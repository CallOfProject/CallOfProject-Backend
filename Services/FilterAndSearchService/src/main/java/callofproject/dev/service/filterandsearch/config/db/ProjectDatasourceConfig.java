package callofproject.dev.service.filterandsearch.config.db;

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

/**
 * This class is used to configure the project database.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "callofproject.dev.data.project",
        entityManagerFactoryRef = "projectEntityManagerFactory",
        transactionManagerRef = "projectDbTransactionManager"
)
public class ProjectDatasourceConfig
{

    /**
     * This method is used to create the project data source.
     *
     * @return the project data source.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.project-db")
    public DataSource projectDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    /**
     * This method is used to create the project entity manager factory.
     *
     * @param dataSource       the project data source.
     * @param jpaVendorAdapter the JPA vendor adapter.
     * @return the project entity manager factory.
     */
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

    /**
     * This method is used to create the project transaction manager.
     *
     * @param entityManagerFactory the project entity manager factory.
     * @return the project transaction manager.
     */
    @Bean
    @Primary
    public PlatformTransactionManager projectDbTransactionManager(@Qualifier("projectEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
