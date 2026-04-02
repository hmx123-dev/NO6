package com.cl.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.YishengyuyueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TongzhiSendService {

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private TongzhijiluService tongzhijiluService;

    public void sendAllNotifications(YishengyuyueEntity yuyue) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String[] reminderTypes = {"预约成功提醒", "就诊前24小时提醒", "就诊前1小时提醒"};
            String[] reminderContents = {
                "尊敬的用户，您的预约已成功！就诊时间：" + sdf.format(yuyue.getYuyueshijian()),
                "尊敬的用户，距您的就诊时间还有24小时！就诊时间：" + sdf.format(yuyue.getYuyueshijian()),
                "尊敬的用户，距您的就诊时间还有1小时！请准时就诊！就诊时间：" + sdf.format(yuyue.getYuyueshijian())
            };

            for (int i = 0; i < reminderTypes.length; i++) {
                JiuzhentongzhiEntity tongzhi = new JiuzhentongzhiEntity();
                tongzhi.setTongzhibianhao("TZ" + System.currentTimeMillis() + i);
                tongzhi.setYishengzhanghao(yuyue.getYishengzhanghao());
                tongzhi.setDianhua(yuyue.getDianhua());
                tongzhi.setJiuzhenshijian(yuyue.getYuyueshijian());
                tongzhi.setTongzhishijian(new Date());
                tongzhi.setZhanghao(yuyue.getZhanghao());
                tongzhi.setShouji(yuyue.getShouji());
                tongzhi.setTongzhibeizhu(reminderTypes[i]);
                tongzhi.setAddtime(new Date());
                
                jiuzhentongzhiService.insert(tongzhi);
                
                sendNotification(tongzhi, reminderContents[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotification(JiuzhentongzhiEntity tongzhi, String content) {
        TongzhijiluEntity jilu = new TongzhijiluEntity();
        jilu.setTongzhibianhao(tongzhi.getTongzhibianhao());
        jilu.setZhanghao(tongzhi.getZhanghao());
        jilu.setShouji(tongzhi.getShouji());
        jilu.setTongzhineirong(content);
        jilu.setSongzhuangtai("发送中");
        jilu.setChongshicishu(0);
        jilu.setAddtime(new Date());

        try {
            boolean success = doSend(tongzhi.getShouji(), content);
            
            if (success) {
                jilu.setSongzhuangtai("已发送");
            } else {
                jilu.setSongzhuangtai("发送失败");
                jilu.setCuowuxinxi("发送失败，请稍后重试");
                jilu.setChongshishijian(getNextRetryTime(0));
            }
        } catch (Exception e) {
            jilu.setSongzhuangtai("发送失败");
            jilu.setCuowuxinxi(e.getMessage());
            jilu.setChongshishijian(getNextRetryTime(0));
        }

        tongzhijiluService.insert(jilu);
    }

    private boolean doSend(String phone, String content) {
        try {
            System.out.println("发送通知到 " + phone + ": " + content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Date getNextRetryTime(int retryCount) {
        Calendar cal = Calendar.getInstance();
        int delayMinutes = (int) Math.pow(2, retryCount) * 5;
        cal.add(Calendar.MINUTE, delayMinutes);
        return cal.getTime();
    }

    public void retryFailedNotifications() {
        EntityWrapper<TongzhijiluEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("songzhuangtai", "发送失败")
               .lt("chongshicishu", 5)
               .le("chongshishijian", new Date());

        List<TongzhijiluEntity> list = tongzhijiluService.selectList(wrapper);

        for (TongzhijiluEntity jilu : list) {
            try {
                boolean success = doSend(jilu.getShouji(), jilu.getTongzhineirong());
                
                if (success) {
                    jilu.setSongzhuangtai("已发送");
                    jilu.setCuowuxinxi(null);
                } else {
                    jilu.setChongshicishu(jilu.getChongshicishu() + 1);
                    if (jilu.getChongshicishu() >= 5) {
                        jilu.setSongzhuangtai("重试失败");
                    } else {
                        jilu.setChongshishijian(getNextRetryTime(jilu.getChongshicishu()));
                    }
                }
            } catch (Exception e) {
                jilu.setChongshicishu(jilu.getChongshicishu() + 1);
                jilu.setCuowuxinxi(e.getMessage());
                if (jilu.getChongshicishu() >= 5) {
                    jilu.setSongzhuangtai("重试失败");
                } else {
                    jilu.setChongshishijian(getNextRetryTime(jilu.getChongshicishu()));
                }
            }
            tongzhijiluService.updateById(jilu);
        }
    }
}
