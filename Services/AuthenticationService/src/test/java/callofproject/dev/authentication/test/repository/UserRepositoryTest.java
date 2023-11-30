package callofproject.dev.authentication.test.repository;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static callofproject.dev.authentication.util.Util.*;
import static java.time.LocalDate.now;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest
{
    @Autowired
    private IUserRepository m_userRepository;


    /*@AfterAll
    static void cleanData() throws IOException
    {
        delete(Path.of(TEST_DB_PATH));
    }*/


    /**
     * Test: Find user by username test
     * Result: User should be found
     */
    @Test
    void findUserByEmail_ExistingUser_ShouldReturnUser()
    {
        var user = new User("testuser", "Test", "User", "testuser@mail.com", "test123", now());
        m_userRepository.save(user);

        var foundUser = m_userRepository.findByEmail("testuser@mail.com");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("testuser", foundUser.get().getUsername());
    }

    /**
     * Test: Find user by email test
     * Result: User should not be found
     */
    @Test
    void findUserByEmail_NonExistingUser_ShouldReturnNull()
    {
        var foundUser = m_userRepository.findByEmail("nonexistent@mail.com");

        Assertions.assertTrue(foundUser.isEmpty());
    }

    /**
     * Test: Remove user test
     * Result: User should be deleted
     */
    @Test
    void deleteUser_ExistingUser_ShouldDeleteUser()
    {
        var user = new User("testuser", "Test", "User", "testuser@mail.com", "test123", now());
        m_userRepository.save(user);

        m_userRepository.delete(user);

        var foundUser = m_userRepository.findByEmail("testuser@mail.com");
        Assertions.assertTrue(foundUser.isEmpty());
    }

    /**
     * Test: Update user test
     * Result: User should be updated
     */

    @Test
    void updateUser_ExistingUser_ShouldUpdateUser()
    {
        var user = new User("testuser43", "Test", "User", "testuser@mail.com", "testusreasd@mail.com", "test123", now(), new Role("ROLE_ADMIN"));
        var savedUser = m_userRepository.save(user);


        savedUser.setUsername("newusername");
        savedUser.setEmail("newemail@mail.com");

        m_userRepository.save(savedUser);

        var updatedUser = m_userRepository.findByEmail("newemail@mail.com");
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("newusername", updatedUser.get().getUsername());
    }
}