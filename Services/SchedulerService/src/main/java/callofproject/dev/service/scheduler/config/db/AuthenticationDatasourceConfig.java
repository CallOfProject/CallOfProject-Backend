package callofproject.dev.service.scheduler.config.db;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "callofproject.dev.repository.authentication",
        entityManagerFactoryRef = "authenticationEntityManagerFactory",
        transactionManagerRef = "authenticationDbTransactionManager"
)
public class AuthenticationDatasourceConfig
{

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.auth-db")
    public DataSource authenticationDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties)
    {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean authenticationEntityManagerFactory(@Qualifier("authenticationDataSource") DataSource dataSource, JpaVendorAdapter jpaVendorAdapter)
    {
        JpaProperties jpaProperties = new JpaProperties();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
                jpaVendorAdapter, jpaProperties.getProperties(), null);
        return builder
                .dataSource(dataSource)
                .packages("callofproject.dev.repository.authentication")
                .persistenceUnit("auth_db")
                .build();
    }

    @Bean
    //@ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager authenticationDbTransactionManager(@Qualifier("authenticationEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
