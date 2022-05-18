package com.bigtree.customer.service;

import com.bigtree.customer.model.OTPEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    MailContentBuilder mailContentBuilder;

    public void sendOTP(OTPEmail otpEmail) {
        log.info("Sending otp to customer email {}", otpEmail.getCustomerEmail());
        Map<String, Object> params = new HashMap<>();
        params.put("otp", otpEmail);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(otpEmail.getCustomerEmail());
            helper.setSubject("Your one-time passcode");
            helper.setText(mailContentBuilder.build("otp", params), true);
            javaMailSender.send(mimeMessage);
            log.info("Password Reset OTP has been sent to {}", otpEmail.getCustomerEmail());
        } catch (MessagingException e) {
            log.error("Unable to send password reset otp");
        }
    }

}
