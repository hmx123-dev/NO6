package com.cl.task;

import com.cl.entity.TongzhijiluEntity;
import com.cl.service.TongzhijiluService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 通知定时任务
 * 用于定时发送计划中的通知和重试失败的通知
 */
@Component
public class NotificationTask {

    @Autowired
    private TongzhijiluService tongzhijiluService;

    /**
     * 每分钟检查一次待发送的通知
     * 发送计划时间已到但还未发送的通知
     */
    @Scheduled(cron = "0 * * * * ?")
    public void sendScheduledNotifications() {
        // 查询待发送且计划发送时间已到的通知
        EntityWrapper<TongzhijiluEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("fasongzhuangtai", 0); // 待发送
        wrapper.le("jihuafasongshijian", new Date()); // 计划发送时间已到
        
        List<TongzhijiluEntity> list = tongzhijiluService.selectList(wrapper);
        
        for (TongzhijiluEntity tongzhijilu : list) {
            tongzhijiluService.sendNotification(tongzhijilu);
        }
    }

    /**
     * 每5分钟重试一次发送失败的通知
     * 重试次数小于3次的失败通知
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void retryFailedNotifications() {
        List<TongzhijiluEntity> list = tongzhijiluService.getRetryNotifications();
        
        for (TongzhijiluEntity tongzhijilu : list) {
            tongzhijiluService.retryNotification(tongzhijilu.getId());
        }
    }
}
