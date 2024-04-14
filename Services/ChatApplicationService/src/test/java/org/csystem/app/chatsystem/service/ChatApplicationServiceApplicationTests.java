package org.csystem.app.chatsystem.service;

import org.csystem.app.chatsystem.service.dto.UserDTO;
import org.csystem.app.chatsystem.service.dto.UserSaveDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static java.util.stream.IntStream.rangeClosed;
import static org.csystem.app.chatsystem.service.BeanName.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
@SpringBootTest
@ComponentScan(basePackages = {REPOSITORY_PACKAGE, SERVICE_PACKAGE})
@EntityScan(basePackages = REPOSITORY_PACKAGE)
@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
class ChatApplicationServiceApplicationTests
{
    @Autowired
    private ChatSystemService m_chatSystemService;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    private UserDTO m_userDTO;

    @BeforeEach
    void setUp()
    {
        var saveDTO = new UserSaveDTO("Nuri Can", "nuricanozturk", "pass123");
        m_userDTO = m_chatSystemService.saveUser(saveDTO);

        rangeClosed(1, 6).forEach(i -> m_chatSystemService.saveLoginInfoByNickName("nuricanozturk"));
    }

    @Test
    void testSaveUser_withGivenUserSaveDTO_shouldReturnUserDTO()
    {
        var saveDTO = new UserSaveDTO("Halil Can", "halilcanozturk", "pass123");
        var userDTO = m_chatSystemService.saveUser(saveDTO);

        assertNotNull(userDTO);

        assertEquals("halilcanozturk", userDTO.nickName());
        assertEquals("Halil Can", userDTO.name());
    }


    @Test
    void testSaveLoginInfo_withGivenValidUsername_shouldLoginInfoDTO()
    {
        var saveDTO = new UserSaveDTO("Ali Can", "alican", "pass123");
        var result = m_chatSystemService.saveUser(saveDTO);

        assertNotNull(result);

        var saveLoginInfoResult = m_chatSystemService.saveLoginInfoByNickName("alican");

        assertNotNull(saveLoginInfoResult);

        assertNotNull(saveLoginInfoResult);
        assertEquals("alican", saveLoginInfoResult.nickname());
    }

    @Test
    void testFindUserByNickName_withGivenValidNickName_shouldReturnUserDTO()
    {
        var userInfo = m_chatSystemService.findUserByNickName("nuricanozturk");

        assertTrue(userInfo.isPresent());

        assertNotNull(userInfo.get());
        assertEquals("nuricanozturk", userInfo.get().nickName());
    }


    @Test
    void testFindUserByNickNameAndPassword_withGivenValidInfo_shouldReturnUserDTO()
    {
        var userInfo = m_chatSystemService.findUserByNickNameAndPassword("nuricanozturk", "pass123");

        assertTrue(userInfo.isPresent());

        assertNotNull(userInfo.get());
        assertEquals("nuricanozturk", userInfo.get().nickName());
    }


    @Test
    void testFindLoginInfoByUserNickName_withGivenValidNickName_shouldReturnLoginInfoDTO()
    {
        var loginInfoDTO = m_chatSystemService.findLoginInfoByUserNickName("nuricanozturk");
        assertTrue(loginInfoDTO.isPresent());
        assertEquals(6, loginInfoDTO.get().loginInfos().size());
    }

    @Test
    void testFindLoginInfoByDateTime_withGivenValidDateTime_shouldReturnLoginInfoDTO()
    {
        var loginInfoDTO = m_chatSystemService.findLoginInfoByDateTime(LocalDate.now());
        assertTrue(loginInfoDTO.isPresent());
        assertEquals(6, loginInfoDTO.get().loginInfos().size());
    }

    @Test
    void testExistsUserByNickname_withGivenValidNickName_shouldReturnTrue()
    {
        assertTrue(m_chatSystemService.existsUserByNickname("nuricanozturk"));
    }

    @Test
    void testExistsUserByNickNameAndPassword_withGivenValidNickNameAndPassword_shouldReturnTrue()
    {
        assertTrue(m_chatSystemService.existsUserByNickNameAndPassword("nuricanozturk", "pass123"));
    }

    @AfterEach
    void clearDatabase()
    {
        m_databaseCleaner.clear();
    }
}