package callofproject.dev.usermanagement.config;

import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jca.support.LocalConnectionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static callofproject.dev.usermanagement.Util.REPO_PACKAGE;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages = REPO_PACKAGE)
public class UserManagementDbConfig
{
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "")
    public DataSource dataSource()
    {
        return DataSourceBuilder.create().build();
    }

    public LocalConnectionFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource)
    {

    }
}
