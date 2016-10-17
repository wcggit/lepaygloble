package com.jifenke.lepluslive.global.config;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Created by xf on 2016/10/17.
 */
@Configuration
public class ActivityConfiguration {

    private final Logger log = LoggerFactory.getLogger(ActivityConfiguration.class);

    /*@Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver hbDownloadTemplateResolver = new ClassLoaderTemplateResolver();
        hbDownloadTemplateResolver.setPrefix("templates/");
        hbDownloadTemplateResolver.setSuffix(".html");
        hbDownloadTemplateResolver.setTemplateMode("HTML5");
        hbDownloadTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        hbDownloadTemplateResolver.setOrder(1);
        return hbDownloadTemplateResolver;
    }

    @Bean
    @Description("Spring mail message resolver")
    public MessageSource emailMessageSource() {
        log.info("loading non-reloadable mail messages resources");
        ReloadableResourceBundleMessageSource
            messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/templates/hbDownload");
        messageSource.setDefaultEncoding(CharEncoding.UTF_8);
        return messageSource;
    }
*/
}
