package callofproject.dev.service.scheduler.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "callofproject.dev.data.project",
        entityManagerFactoryRef = "projectEntityManagerFactory",
        transactionManagerRef = "projectDbTransactionManager"
)
public class ProjectDatasourceConfig
{

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.project-db")
    public DataSource projectDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean projectEntityManagerFactory(@Qualifier("projectDataSource") DataSource dataSource, JpaVendorAdapter jpaVendorAdapter)
    {
        JpaProperties jpaProperties = new JpaProperties();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);

        return builder
                .dataSource(dataSource)
                .packages("callofproject.dev.data.project")
                .persistenceUnit("project_db")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager projectDbTransactionManager(@Qualifier("projectEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

