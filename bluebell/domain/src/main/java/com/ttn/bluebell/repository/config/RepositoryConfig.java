package com.ttn.bluebell.repository.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.ttn.bluebell.repository")
@ComponentScan(basePackages = { "com.ttn.bluebell.repository"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,pattern = ".*Test.*")
)
@PropertySource("classpath:application.properties")
public class RepositoryConfig {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    private static final String PROPERTY_NAME_GENERATE_DDL = "generate.ddl";
    public static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    public static final String PROPERTY_NAME_DS_INITIALIZER = "ds.initializer";

    @Resource
    private Environment env;

    @Value("classpath:data.sql")
    private org.springframework.core.io.Resource dataScript;

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(env.getProperty(PROPERTY_NAME_GENERATE_DDL, Boolean.class, false));
        jpaVendorAdapter.setShowSql(env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL, Boolean.class, false));
        jpaVendorAdapter.setDatabasePlatform(env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
        lemfb.setDataSource(dataSource);
        lemfb.setJpaVendorAdapter(jpaVendorAdapter);
        // TOD: Need to setup a way to override based on provider if required
        Properties providerProperties = hibProperties();
        lemfb.setJpaProperties(providerProperties);
        String[] packagesToScan = packagesToScan();
        lemfb.setPackagesToScan(packagesToScan);
        return lemfb;
    }

    protected String[] packagesToScan() {
        String pkgToScan = "org.springframework.data.jpa.convert.threeten," + env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN);
        String[] packagesToScan = pkgToScan.split(",");
        return packagesToScan;
    }

    protected Properties hibProperties() {
        Properties properties = new Properties();
        properties.put(
                PROPERTY_NAME_HIBERNATE_FORMAT_SQL,
                env.getProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, Boolean.class, false));

        Boolean generateDDL = env.getProperty(PROPERTY_NAME_GENERATE_DDL, Boolean.class, false);
        if (generateDDL) {
            properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        }
        properties.put("hibernate.hbm2ddl.import_files", env.getProperty("application.startup.script"));
        properties.put("hibernate.id.new_generator_mappings", "true");
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        DataSourceInitializer initializer = createDataSourceInitializer(dataSource);
        return initializer;
    }

    protected DataSourceInitializer createDataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        Boolean dsInitializerEnabled = env.getProperty(PROPERTY_NAME_DS_INITIALIZER, Boolean.class, false);
        initializer.setEnabled(dsInitializerEnabled);
        return initializer;
    }

    protected DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        Boolean dsInitializerEnabled = env.getProperty(PROPERTY_NAME_DS_INITIALIZER, Boolean.class, false);
        if(dsInitializerEnabled && dataScript.exists()) {
            populator.addScript(dataScript);
        }
        return populator;
    }

    @Bean
    @DependsOn(value = "dataSourceInitializer")
    public JdbcOperations jdbcOperations(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
