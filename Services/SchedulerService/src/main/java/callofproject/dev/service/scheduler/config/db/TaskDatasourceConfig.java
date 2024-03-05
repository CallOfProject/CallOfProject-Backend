package callofproject.dev.service.scheduler.config.db;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
        basePackages = "callofproject.dev.data.task",
        entityManagerFactoryRef = "taskEntityManagerFactory",
        transactionManagerRef = "taskDbTransactionManager"
)
public class TaskDatasourceConfig
{

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.task-db")
    public DataSource taskDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "vendor.task")
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties)
    {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean taskEntityManagerFactory(@Qualifier("taskDataSource") DataSource dataSource,
                                                                           @Qualifier("vendor.task") JpaVendorAdapter jpaVendorAdapter)
    {
        JpaProperties jpaProperties = new JpaProperties();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(
                jpaVendorAdapter, jpaProperties.getProperties(), null);
        return builder
                .dataSource(dataSource)
                .packages("callofproject.dev.data.task")
                .persistenceUnit("task_db")
                .build();
    }

    @Bean
    //@ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager taskDbTransactionManager(@Qualifier("taskEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
