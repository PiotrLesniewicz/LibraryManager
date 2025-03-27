package org.library.infrastructure.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Environment;
import org.library.infrastructure.database.entity._EntityMarker;
import org.library.infrastructure.database.repository._JpaRepositoriesMarker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackageClasses = _JpaRepositoriesMarker.class)
@EnableTransactionManagement
@RequiredArgsConstructor
@PropertySource({"classpath:database.properties"})
public class PersistenceJpaRepositories {

    private final org.springframework.core.env.Environment environment;

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaProperties(jpaProperties());
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPackagesToScan(_EntityMarker.class.getPackageName());
        return entityManagerFactory;
    }

    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty(Environment.HBM2DDL_AUTO, environment.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty(Environment.SHOW_SQL, environment.getProperty("hibernate.show_sql"));
        properties.setProperty(Environment.FORMAT_SQL, environment.getProperty("hibernate.format_sql"));
        return properties;
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.user"));
        dataSource.setPassword(environment.getProperty("jdbc.pass"));
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setPoolName("springHikariCP");
        dataSource.setMaximumPoolSize(20);
        dataSource.setConnectionTimeout(20000);
        dataSource.setMinimumIdle(10);
        dataSource.setIdleTimeout(10000);
        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean(initMethod = "migrate")
    Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .locations("classpath:flyway/migrations")
                .dataSource(dataSource)
                .load();
    }
}
