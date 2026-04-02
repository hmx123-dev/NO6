package com.cl.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.TongzhijiluDao;
import com.cl.entity.TongzhijiluEntity;
import com.cl.service.TongzhijiluService;
import com.cl.entity.view.TongzhijiluView;

@Service("tongzhijiluService")
public class TongzhijiluServiceImpl extends ServiceImpl<TongzhijiluDao, TongzhijiluEntity> implements TongzhijiluService {

    @Autowired
    private TongzhijiluDao tongzhijiluDao;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TongzhijiluEntity> page = this.selectPage(
                new Query<TongzhijiluEntity>(params).getPage(),
                new EntityWrapper<TongzhijiluEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<TongzhijiluEntity> wrapper) {
		  Page<TongzhijiluView> page =new Query<TongzhijiluView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<TongzhijiluView> selectListView(Wrapper<TongzhijiluEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public TongzhijiluView selectView(Wrapper<TongzhijiluEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public void createAppointmentNotifications(String yuyuebianhao, String yishengzhanghao, String zhanghao, Date jiuzhenshijian) {
		// 预约成功后立即创建所有后续通知记录
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 1. 预约成功通知 - 立即发送
		TongzhijiluEntity successNotify = new TongzhijiluEntity<>();
		successNotify.setYuyuebianhao(yuyuebianhao);
		successNotify.setYishengzhanghao(yishengzhanghao);
		successNotify.setZhanghao(zhanghao);
		successNotify.setTongzhileixing(1);
		successNotify.setTongzhineirong("您的预约已成功！就诊时间：" + sdf.format(jiuzhenshijian) + "，请准时到达。");
		successNotify.setJihuafasongshijian(new Date());
		successNotify.setJiuzhenshijian(jiuzhenshijian);
		successNotify.setFasongzhuangtai(0);
		successNotify.setChongshicishu(0);
		successNotify.setJieshouzhuangtai(0);
		this.insert(successNotify);
		// 立即发送
		sendNotification(successNotify);
		
		// 2. 就诊前一天提醒
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(jiuzhenshijian);
		cal1.add(Calendar.DAY_OF_MONTH, -1);
		cal1.set(Calendar.HOUR_OF_DAY, 9);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		
		TongzhijiluEntity oneDayBefore = new TongzhijiluEntity<>();
		oneDayBefore.setYuyuebianhao(yuyuebianhao);
		oneDayBefore.setYishengzhanghao(yishengzhanghao);
		oneDayBefore.setZhanghao(zhanghao);
		oneDayBefore.setTongzhileixing(2);
		oneDayBefore.setTongzhineirong("明天（" + sdf.format(jiuzhenshijian) + "）是您的就诊时间，请做好准备。");
		oneDayBefore.setJihuafasongshijian(cal1.getTime());
		oneDayBefore.setJiuzhenshijian(jiuzhenshijian);
		oneDayBefore.setFasongzhuangtai(0);
		oneDayBefore.setChongshicishu(0);
		oneDayBefore.setJieshouzhuangtai(0);
		this.insert(oneDayBefore);
		// 如果计划发送时间就是现在或已过，立即发送
		if (cal1.getTime().compareTo(new Date()) <= 0) {
			sendNotification(oneDayBefore);
		}
		
		// 3. 就诊当天提醒
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(jiuzhenshijian);
		cal2.set(Calendar.HOUR_OF_DAY, 8);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		
		TongzhijiluEntity sameDay = new TongzhijiluEntity<>();
		sameDay.setYuyuebianhao(yuyuebianhao);
		sameDay.setYishengzhanghao(yishengzhanghao);
		sameDay.setZhanghao(zhanghao);
		sameDay.setTongzhileixing(3);
		sameDay.setTongzhineirong("今天（" + sdf.format(jiuzhenshijian) + "）是您的就诊时间，请准时到达医院。");
		sameDay.setJihuafasongshijian(cal2.getTime());
		sameDay.setJiuzhenshijian(jiuzhenshijian);
		sameDay.setFasongzhuangtai(0);
		sameDay.setChongshicishu(0);
		sameDay.setJieshouzhuangtai(0);
		this.insert(sameDay);
		// 如果计划发送时间就是现在或已过，立即发送
		if (cal2.getTime().compareTo(new Date()) <= 0) {
			sendNotification(sameDay);
		}
		
		// 4. 就诊后随访提醒
		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(jiuzhenshijian);
		cal3.add(Calendar.DAY_OF_MONTH, 1);
		cal3.set(Calendar.HOUR_OF_DAY, 10);
		cal3.set(Calendar.MINUTE, 0);
		cal3.set(Calendar.SECOND, 0);
		
		TongzhijiluEntity followUp = new TongzhijiluEntity<>();
		followUp.setYuyuebianhao(yuyuebianhao);
		followUp.setYishengzhanghao(yishengzhanghao);
		followUp.setZhanghao(zhanghao);
		followUp.setTongzhileixing(4);
		followUp.setTongzhineirong("您已于昨天完成就诊，如有不适请及时复诊。祝您早日康复！");
		followUp.setJihuafasongshijian(cal3.getTime());
		followUp.setJiuzhenshijian(jiuzhenshijian);
		followUp.setFasongzhuangtai(0);
		followUp.setChongshicishu(0);
		followUp.setJieshouzhuangtai(0);
		this.insert(followUp);
		// 如果计划发送时间就是现在或已过，立即发送
		if (cal3.getTime().compareTo(new Date()) <= 0) {
			sendNotification(followUp);
		}
	}
	
	@Override
	public boolean sendNotification(TongzhijiluEntity tongzhijilu) {
		try {
			// 模拟发送通知（实际项目中这里调用短信接口、推送服务等）
			boolean sendSuccess = doSend(tongzhijilu);
			
			if (sendSuccess) {
				tongzhijilu.setFasongzhuangtai(1);
				tongzhijilu.setFasongshijian(new Date());
				tongzhijilu.setShibaiyuanyin(null);
			} else {
				tongzhijilu.setFasongzhuangtai(2);
				tongzhijilu.setShibaiyuanyin("发送服务返回失败");
			}
			
			this.updateById(tongzhijilu);
			return sendSuccess;
			
		} catch (Exception e) {
			tongzhijilu.setFasongzhuangtai(2);
			tongzhijilu.setShibaiyuanyin(e.getMessage());
			this.updateById(tongzhijilu);
			return false;
		}
	}
	
	/**
	 * 实际发送通知的方法（模拟）
	 */
	private boolean doSend(TongzhijiluEntity tongzhijilu) {
		// 这里模拟发送通知
		// 实际项目中应该调用短信API、推送服务等
		// 例如：阿里云短信、腾讯云短信、极光推送等
		
		// 模拟随机失败（用于测试重试机制）
		// return Math.random() > 0.2;
		
		// 默认返回成功
		return true;
	}
	
	@Override
	public boolean retryNotification(Long id) {
		TongzhijiluEntity tongzhijilu = this.selectById(id);
		if (tongzhijilu == null) {
			return false;
		}
		
		// 增加重试次数
		tongzhijilu.setChongshicishu(tongzhijilu.getChongshicishu() + 1);
		
		// 重新发送
		boolean success = sendNotification(tongzhijilu);
		
		// 更新重试次数
		this.updateById(tongzhijilu);
		
		return success;
	}
	
	@Override
	public void updateReceiveStatus(Long id, Integer status) {
		TongzhijiluEntity tongzhijilu = this.selectById(id);
		if (tongzhijilu != null) {
			tongzhijilu.setJieshouzhuangtai(status);
			tongzhijilu.setJieshoushijian(new Date());
			this.updateById(tongzhijilu);
		}
	}
	
	@Override
	public List<TongzhijiluEntity> getRetryNotifications() {
		// 获取发送失败且重试次数小于3次的通知
		EntityWrapper<TongzhijiluEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("fasongzhuangtai", 2);
		wrapper.lt("chongshicishu", 3);
		return this.selectList(wrapper);
	}
}
