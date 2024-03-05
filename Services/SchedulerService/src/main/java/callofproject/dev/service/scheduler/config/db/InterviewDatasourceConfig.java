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
        basePackages = "callofproject.dev.data.interview",
        entityManagerFactoryRef = "interviewEntityManagerFactory",
        transactionManagerRef = "interviewDbTransactionManager"
)
public class InterviewDatasourceConfig
{

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.interview-db")
    public DataSource interviewDataSource()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "vendor.interview")
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties)
    {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean interviewEntityManagerFactory(@Qualifier("interviewDataSource") DataSource dataSource, @Qualifier("vendor.interview") JpaVendorAdapter jpaVendorAdapter)
    {
        var jpaProperties = new JpaProperties();
        var builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);

        return builder
                .dataSource(dataSource)
                .packages("callofproject.dev.data.interview")
                .persistenceUnit("interview_db")
                .build();
    }

    @Bean(name = "interviewDbTransactionManager")
    public PlatformTransactionManager interviewDbTransactionManager(@Qualifier("interviewEntityManagerFactory") EntityManagerFactory entityManagerFactory)
    {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
