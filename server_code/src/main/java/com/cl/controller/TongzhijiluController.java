package com.cl.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.SysLog;
import com.cl.entity.TongzhijiluEntity;
import com.cl.service.TongzhijiluService;
import com.cl.service.TongzhiSendService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;

@RestController
@RequestMapping("/tongzhijilu")
public class TongzhijiluController {
    @Autowired
    private TongzhijiluService tongzhijiluService;
    @Autowired
    private TongzhiSendService tongzhiSendService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TongzhijiluEntity tongzhijilu) {
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<>();
        PageUtils page = tongzhijiluService.queryPage(params, ew);
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
        return R.ok().put("data", tongzhijilu);
    }

    @RequestMapping("/save")
    @SysLog("新增通知记录")
    public R save(@RequestBody TongzhijiluEntity tongzhijilu) {
        tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }

    @RequestMapping("/update")
    @SysLog("修改通知记录")
    public R update(@RequestBody TongzhijiluEntity tongzhijilu) {
        tongzhijiluService.updateById(tongzhijilu);
        return R.ok();
    }

    @RequestMapping("/delete")
    @SysLog("删除通知记录")
    public R delete(@RequestBody Long[] ids) {
        tongzhijiluService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/retry")
    @SysLog("重试发送通知")
    public R retry(@RequestBody Long[] ids) {
        for (Long id : ids) {
            TongzhijiluEntity jilu = tongzhijiluService.selectById(id);
            if (jilu != null && ("发送失败".equals(jilu.getSongzhuangtai()) || "重试失败".equals(jilu.getSongzhuangtai()))) {
                jilu.setSongzhuangtai("发送中");
                jilu.setChongshicishu(0);
                jilu.setCuowuxinxi(null);
                tongzhijiluService.updateById(jilu);
                tongzhiSendService.sendNotification(null, jilu.getTongzhineirong());
            }
        }
        tongzhiSendService.retryFailedNotifications();
        return R.ok();
    }
}
