package com.bigtree.chef.orders.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.bigtree.chef.orders.resources.ResourceConfig;
import com.bigtree.chef.orders.entity.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MailContentBuilder mailContentBuilder;

    @Autowired
    ResourceConfig resourceConfig;

    public void sendOrderConfirmation(Order order) {
        log.info("Sending order confirmation customer email {} to {}", order.getReference(), order.getCustomerEmail());
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("count", order.getItems().size());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
        params.put("today", dateFormat.format(cal.getTime()));
        params.put("deliveryDate", dateFormat.format(cal.getTime()));
        sendMail(order.getCustomerEmail(), "BEKU Order Confirmation", "order", params);
    }

    public void sendMail(String to, String subject, String template, Map<String, Object> params) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mailContentBuilder.build(template, params), true);
            javaMailSender.send(mimeMessage);
            log.info("Order confirmation email sent to {}", to);
        } catch (MessagingException e) {
            log.error("Order confirmation email not sent!");
        }

    }

}
