package callofproject.dev.authentication.test.repository;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.StreamSupport;

import static callofproject.dev.authentication.util.Util.*;
import static java.nio.file.Files.delete;
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


    @AfterAll
    static void cleanData() throws IOException
    {
        delete(Path.of(TEST_DB_PATH));
    }

    @Test
    void saveMultipleUsers_ShouldSucceed()
    {
        var ahmet = new User("ahmet", "ahmet", "", "koc", "ahmet@mail.com", "ahmet123", now(), new Role("ROLE_ADMIN"));
        var emir = new User("emir", "emir", "", "Kafadar", "emir@mail.com", "emir321", now(), new Role("ROLE_ADMIN"));
        var nurican = new User("nuri", "nuri", "can", "öztürk", "nurican@mail.com", "nuri231", now(), new Role("ROLE_ADMIN"));

        m_userRepository.saveAll(List.of(ahmet, emir, nurican));
        var userList = StreamSupport.stream(m_userRepository.findAll().spliterator(), false).toList();
        Assertions.assertEquals(3, userList.size());
    }

    @Test
    void findUserByEmail_ExistingUser_ShouldReturnUser()
    {
        var user = new User("testuser", "Test", "User", "testuser@mail.com", "test123", now());
        m_userRepository.save(user);

        var foundUser = m_userRepository.findByEmail("testuser@mail.com");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findUserByEmail_NonExistingUser_ShouldReturnNull()
    {
        var foundUser = m_userRepository.findByEmail("nonexistent@mail.com");

        Assertions.assertTrue(foundUser.isEmpty());
    }

    @Test
    void deleteUser_ExistingUser_ShouldDeleteUser()
    {
        var user = new User("testuser", "Test", "User", "testuser@mail.com", "test123", now());
        m_userRepository.save(user);

        m_userRepository.delete(user);

        var foundUser = m_userRepository.findByEmail("testuser@mail.com");
        Assertions.assertTrue(foundUser.isEmpty());
    }

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