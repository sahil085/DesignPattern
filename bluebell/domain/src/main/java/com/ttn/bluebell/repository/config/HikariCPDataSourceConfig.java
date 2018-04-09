package com.ttn.bluebell.repository.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class HikariCPDataSourceConfig {
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
    private static final String PROPERTY_NAME_POOL_SIZE = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE=15;

    @Resource
    private Environment env;

    protected HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        config.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        config.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        config.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        Integer poolSize = env.getProperty(PROPERTY_NAME_POOL_SIZE,Integer.class,DEFAULT_POOL_SIZE);
        config.setMaximumPoolSize(poolSize);
        return config;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = hikariConfig();
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }
}
