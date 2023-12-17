package callofproject.dev.project.service;

import callofproject.dev.project.DatabaseCleaner;
import callofproject.dev.project.Injection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static callofproject.dev.project.util.Constants.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {PROJECT_SERVICE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class AdminServiceTest
{
    @Autowired
    private Injection m_injection;
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @BeforeEach
    public void setUpAndCheckUsers()
    {

    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
