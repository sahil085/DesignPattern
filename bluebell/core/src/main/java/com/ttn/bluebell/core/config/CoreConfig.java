package com.ttn.bluebell.core.config;

import com.ttn.bluebell.core.scheduler.SchedulerService;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.dozer.DozerBeanMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.ui.velocity.VelocityEngineFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@EnableCaching
@EnableAsync
@EnableScheduling
@Configuration
@ComponentScan(basePackages = {"com.ttn.bluebell.core"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,pattern = ".*Test.*")
)
@PropertySource("classpath:application.properties")
public class CoreConfig {

    @Resource
    private Environment env;

    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getRequiredProperty("email.service.host"));
        mailSender.setPort(Integer.parseInt(env.getRequiredProperty("email.service.port")));
        mailSender.setUsername(env.getRequiredProperty("email.service.admin.name"));
        mailSender.setPassword(env.getRequiredProperty("email.service.admin.password"));
        Properties javaMailProperties = new Properties();
        if("smtp.gmail.com".equalsIgnoreCase(env.getRequiredProperty("email.service.host"))) {
            javaMailProperties.put("mail.smtp.starttls.enable", "true");
            javaMailProperties.put("mail.smtp.auth", "true");
            javaMailProperties.put("mail.transport.protocol", "smtp");
            javaMailProperties.put("mail.debug", "true");
        } else {
            javaMailProperties.put("mail.smtp.starttls.enable", "true");
            javaMailProperties.put("mail.smtp.port", Integer.parseInt(env.getRequiredProperty("email.service.port")));
            javaMailProperties.put("mail.smtp.auth", "true");
            javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
        }
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public VelocityEngine getVelocityEngine() throws VelocityException, IOException {
        VelocityEngineFactory factory = new VelocityEngineFactory();
        Properties props = new Properties();
        props.put("resource.loader", "class");
        props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        factory.setVelocityProperties(props);
        return factory.createVelocityEngine();
    }

    @Bean
    public DozerBeanMapper beanMapper () {
        List<String> mappingFiles = new ArrayList();
        mappingFiles.add("dozerJdk8Converters.xml");
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.setMappingFiles(mappingFiles);
        return mapper;
    }

    @Bean
    public SchedulerService schedulerService(){
        return new SchedulerService();
    }
}
