package com.bigtree.chef.orders;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class ChefOrderServiceApplication {

    public static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    @Value("${smtp.server}")
    private String smtpServer;
    @Value("${smtp.username}")
    private String smtpUsername;
    @Value("${smtp.password}")
    private String smtpPassword;
    @Value("${smtp.debug}")
    private String smtpMailDebug;

    public static void main(String[] args) {
        SpringApplication.run(ChefOrderServiceApplication.class, args);
    }

    @Bean(name = "htmlTemplateResolver")
    public ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        // templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    @Bean(name = "javaMailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpServer);
        mailSender.setPort(587);

        mailSender.setUsername(smtpUsername);
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", smtpMailDebug);

        return mailSender;
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

}
