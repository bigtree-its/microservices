package com.bigtree.chef.orders.controller;

import com.bigtree.chef.orders.model.SMS;
import com.bigtree.chef.orders.service.SMSService;
import com.twilio.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
public class SMSController {

    @Autowired
    SimpMessagingTemplate webSocket;

    private final String  TOPIC_DESTINATION = "/topic/sms";

    @Autowired
    SMSService service;

    @CrossOrigin(origins = "*")
    @PostMapping("/sms")
    public void sendSms(@RequestBody SMS sms) {
        log.info("Received request to send sms to {}", sms.getTo());
        try{
            service.send(sms);
        }
        catch(ApiException e){
            webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": Error sending the SMS: "+e.getMessage());
            throw e;
        }
        webSocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent!: "+sms.getTo());

    }

    private String getTimeStamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }
}
