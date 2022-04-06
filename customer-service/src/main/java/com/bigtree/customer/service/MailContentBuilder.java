package com.bigtree.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class MailContentBuilder {
 
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    @Qualifier("htmlTemplateResolver")
    ITemplateResolver htmlTemplateResolver;

    private boolean templateEngineInitialized;

    public String build(String template, Map<String,Object> params) {
        if (! this.templateEngineInitialized){
            templateEngine.setTemplateResolver(htmlTemplateResolver);
            this.templateEngineInitialized = true;
        }
        Context context = new Context();
        context.setLocale(Locale.UK);
        if (!CollectionUtils.isEmpty(params)){
            context.setVariables(params);
            params.forEach((k,v) -> {
                log.info("Adding Key {}:{} into mail message", k, v);
            });
        }
        return templateEngine.process(template, context);
    }
 
}
