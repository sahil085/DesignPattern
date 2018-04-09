package com.ttn.bluebell.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.base.Predicates;
import com.ttn.bluebell.durable.model.constant.RabbitMqQueueNameConstant;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

@Configuration
@ComponentScan(basePackages = {"com.ttn.bluebell.rest", "com.ttn.bluebell.web"
})
@EnableCaching
@EnableSwagger2
public class AppConfig extends WebMvcConfigurerAdapter {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.dateFormat(new ISO8601DateFormat());
        return builder;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Queue queue() {
        return new Queue(RabbitMqQueueNameConstant.changeMentovisorQueue);
    }

    @Bean
    public Queue projectInfoQueue() {
        return new Queue(RabbitMqQueueNameConstant.PROJECT_INFO_QUEUE);
    }

    @Bean
    public Queue coreQueue() {
        return new Queue(RabbitMqQueueNameConstant.updateCoreEntityQueue);
    }

    @Bean
    public TopicExchange coreEntityExchange() {
        return new TopicExchange(RabbitMqQueueNameConstant.updateCoreEntityExchange);
    }

    @Bean
    public TopicExchange projectInfoExchange() {
        return new TopicExchange(RabbitMqQueueNameConstant.PROJECT_INFO_EXCHANGE);
    }

    @Bean
    public Binding projectInfoQueueBindingBuilder() {
        return BindingBuilder.bind(projectInfoQueue()).to(projectInfoExchange()).with(RabbitMqQueueNameConstant.PROJECT_INFO_ROUTING_KEY);
    }

    @Bean
    public Binding coreEntityQueueBindingBuilder() {
        return BindingBuilder.bind(coreQueue()).to(coreEntityExchange()).with(RabbitMqQueueNameConstant.updateCoreEntityRoutingKey);
    }



    @Bean
    public Filter loggingFilter() {
        AbstractRequestLoggingFilter f = new AbstractRequestLoggingFilter() {

            @Override
            protected void beforeRequest(HttpServletRequest request, String message) {
                LOGGER.info("beforeRequest: " + message);
            }

            @Override
            protected void afterRequest(HttpServletRequest request, String message) {
                LOGGER.info("afterRequest: " + message);
            }
        };
        f.setIncludeClientInfo(true);
        f.setIncludePayload(true);
        f.setIncludeQueryString(true);

        f.setBeforeMessagePrefix("BEFORE REQUEST  [");
        f.setAfterMessagePrefix("AFTER REQUEST    [");
        f.setAfterMessageSuffix("]\n");
        return f;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8000/").allowedMethods("*").allowedHeaders("*").allowCredentials(false).maxAge(3600);
    }
}