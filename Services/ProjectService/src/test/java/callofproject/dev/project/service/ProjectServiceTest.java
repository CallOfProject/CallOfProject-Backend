package callofproject.dev.project.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;
import static callofproject.dev.project.util.Constants.TEST_PROPERTIES_FILE;

@SpringBootTest
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EntityScan(basePackages = BASE_PACKAGE_BEAN_NAME)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class ProjectServiceTest
{
}
