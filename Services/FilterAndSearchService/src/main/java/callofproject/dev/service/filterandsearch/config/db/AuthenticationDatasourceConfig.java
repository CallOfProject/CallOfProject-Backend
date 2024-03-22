package callofproject.dev.service.filterandsearch.config.db;


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

/**
 * This class is used to configure the authentication database.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "callofproject.dev.repository.authentication",
        entityManagerFactoryRef = "authenticationEntityManagerFactory",
        transactionManagerRef = "authenticationDbTransactionManager"
)
public class AuthenticationDatasourceConfig
{
    /**
     * This method is used to create the authentication data source.
     *
     * @return the authentication data source.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.auth-db")
    public DataSource authenticationDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    /**
     * This method is used to create the JPA vendor adapter.
     *
     * @param jpaProperties the JPA properties.
     * @return the JPA vendor adapter.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties)
    {
        return new HibernateJpaVendorAdapter();
    }

    /**
     * This method is used to create the authentication entity manager factory.
     *
     * @param dataSource       the authentication data source.
     * @param jpaVendorAdapter the JPA vendor adapter.
     * @return the authentication entity manager factory.
     */
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

    /**
     * This method is used to create the authentication database transaction manager.
     *
     * @param entityManagerFactory the authentication entity manager factory.
     * @return the authentication database transaction manager.
     */
    @Bean
    //@ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager authenticationDbTransactionManager(@Qualifier("authenticationEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}