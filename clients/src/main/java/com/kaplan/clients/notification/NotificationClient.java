package com.kaplan.clients.notification;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("notification")
public interface NotificationClient {

    @PostMapping(path = "api/v1/notification")
    void sendNotification(NotificationRequest notificationRequest);

}