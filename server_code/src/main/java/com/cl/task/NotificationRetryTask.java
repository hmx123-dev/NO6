package com.cl.task;

import com.cl.service.TongzhiSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationRetryTask {

    @Autowired
    private TongzhiSendService tongzhiSendService;

    @Scheduled(fixedRate = 60000)
    public void retryFailedNotifications() {
        try {
            System.out.println("定时任务：开始重试发送失败的通知...");
            tongzhiSendService.retryFailedNotifications();
            System.out.println("定时任务：通知重试完成");
        } catch (Exception e) {
            System.err.println("定时任务：通知重试失败 - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
