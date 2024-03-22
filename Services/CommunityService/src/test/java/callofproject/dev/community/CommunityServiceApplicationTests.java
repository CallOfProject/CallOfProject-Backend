package callofproject.dev.community;

import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static callofproject.dev.community.CommunityServiceBeanName.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPOSITORY_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
class CommunityServiceApplicationTests
{
    @Autowired
    private Injection m_injection;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    private User user1;
    private User user2;

    @BeforeEach
    void contextLoads()
    {
        var user_1 = new User(UUID.randomUUID(), "nuri", "nuri@gmail.com", "Nuri", "Can", "Ozturk", null);
        var user_2 = new User(UUID.randomUUID(), "halil", "halil@gmail.com", "Halil", "Can", "Ozturk", null);

        user1 = m_injection.getCommunityServiceHelper().upsertUser(user_1);
        user2 = m_injection.getCommunityServiceHelper().upsertUser(user_2);
    }


    @Test
    void testCreateConnection_withGivenValidTwoUserId_shouldReturnRequest()
    {
        var request = m_injection.getConnectionService().sendConnectionRequest(user1.getUserId(), user2.getUserId());
        assertNotNull(request);
        assertEquals(Status.OK, request.getStatusCode());
        assertTrue((Boolean) request.getObject());
    }

    @Test
    void testCreateConnection_withGivenInvalidTwoUserId_shouldThrowDataServiceException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getConnectionService().sendConnectionRequest(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void answerConnectionRequest_withGivenValidUserIdsAndPositiveAnswer_shouldReturnRequest()
    {
        // Send connection request
        var request = m_injection.getConnectionService().sendConnectionRequest(user1.getUserId(), user2.getUserId());
        assertNotNull(request);
        assertEquals(Status.OK, request.getStatusCode());
        assertTrue((Boolean) request.getObject());

        // Accept connection request
        var answer = m_injection.getConnectionService().answerConnectionRequest(user1.getUserId(), user2.getUserId(), true);
        assertNotNull(answer);
        assertEquals(Status.OK, answer.getStatusCode());
        assertTrue((Boolean) answer.getObject());

        // Check connections
        var connectedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        var connectedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertNotNull(connectedUser1);
        assertNotNull(connectedUser2);

        assertEquals(1, connectedUser1.getConnections().size());
        assertEquals(1, connectedUser2.getConnections().size());
        assertEquals(0, connectedUser1.getConnectionRequests().size());
        assertEquals(0, connectedUser2.getConnectionRequests().size());
    }


    @Test
    void answerConnectionRequest_withGivenValidUserIdsAndNegativeAnswer_shouldReturnRequest()
    {
        // Send connection request
        var request = m_injection.getConnectionService().sendConnectionRequest(user1.getUserId(), user2.getUserId());
        assertNotNull(request);
        assertEquals(Status.OK, request.getStatusCode());
        assertTrue((Boolean) request.getObject());

        // Accept connection request
        var answer = m_injection.getConnectionService().answerConnectionRequest(user1.getUserId(), user2.getUserId(), false);
        assertNotNull(answer);
        assertEquals(Status.OK, answer.getStatusCode());
        assertFalse((Boolean) answer.getObject());

        // Check connections
        var connectedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        var connectedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertNotNull(connectedUser1);
        assertNotNull(connectedUser2);

        assertEquals(0, connectedUser1.getConnections().size());
        assertEquals(0, connectedUser2.getConnections().size());
        assertEquals(0, connectedUser1.getConnectionRequests().size());
        assertEquals(0, connectedUser2.getConnectionRequests().size());
    }

    @Test
    void answerConnectionRequest_withGivenInvalidUserIdsAndAnswer_shouldThrowDataServiceException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getConnectionService().answerConnectionRequest(UUID.randomUUID(), UUID.randomUUID(), true));
    }

    @Test
    void testRemoveConnection_withGivenValidUserIds_shouldReturnRequest()
    {
        // Send connection request
        var request = m_injection.getConnectionService().sendConnectionRequest(user1.getUserId(), user2.getUserId());
        assertNotNull(request);
        assertEquals(Status.OK, request.getStatusCode());
        assertTrue((Boolean) request.getObject());

        // Accept connection request
        var answer = m_injection.getConnectionService().answerConnectionRequest(user1.getUserId(), user2.getUserId(), true);
        assertNotNull(answer);
        assertEquals(Status.OK, answer.getStatusCode());
        assertTrue((Boolean) answer.getObject());

        // Check connections
        var connectedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        var connectedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertEquals(1, connectedUser1.getConnections().size());
        assertEquals(1, connectedUser2.getConnections().size());
        assertEquals(0, connectedUser1.getConnectionRequests().size());
        assertEquals(0, connectedUser2.getConnectionRequests().size());

        // Remove connection
        var remove = m_injection.getConnectionService().removeConnection(user1.getUserId(), user2.getUserId());
        assertNotNull(remove);
        assertEquals(Status.OK, remove.getStatusCode());
        assertTrue((Boolean) remove.getObject());

        // Check connections
        connectedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        connectedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertNotNull(connectedUser1);
        assertNotNull(connectedUser2);

        assertEquals(0, connectedUser1.getConnections().size());
        assertEquals(0, connectedUser2.getConnections().size());
        assertEquals(0, connectedUser1.getConnectionRequests().size());
        assertEquals(0, connectedUser2.getConnectionRequests().size());
    }


    @Test
    void testBlockUser_withGivenValidUserIds_shouldReturnRequest()
    {
        // Send connection request
        var request = m_injection.getConnectionService().sendConnectionRequest(user1.getUserId(), user2.getUserId());
        assertNotNull(request);
        assertEquals(Status.OK, request.getStatusCode());
        assertTrue((Boolean) request.getObject());

        // Accept connection request
        var answer = m_injection.getConnectionService().answerConnectionRequest(user1.getUserId(), user2.getUserId(), true);
        assertNotNull(answer);
        assertEquals(Status.OK, answer.getStatusCode());
        assertTrue((Boolean) answer.getObject());

        // Check connections
        var connectedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        var connectedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertEquals(1, connectedUser1.getConnections().size());
        assertEquals(1, connectedUser2.getConnections().size());
        assertEquals(0, connectedUser1.getConnectionRequests().size());
        assertEquals(0, connectedUser2.getConnectionRequests().size());

        // Block user
        var block = m_injection.getConnectionService().blockConnection(user1.getUserId(), user2.getUserId());
        assertNotNull(block);
        assertEquals(Status.OK, block.getStatusCode());
        assertTrue((Boolean) block.getObject());

        // Check blocked users
        var blockedUser1 = m_injection.getConnectionService().findUserByIdIfExist(user1.getUserId());
        var blockedUser2 = m_injection.getConnectionService().findUserByIdIfExist(user2.getUserId());

        assertNotNull(blockedUser1);
        assertNotNull(blockedUser2);

        assertEquals(1, blockedUser1.getBlockedConnections().size());
        assertEquals(1, blockedUser2.getBlockedConnections().size());
        assertEquals(0, blockedUser1.getConnections().size());
        assertEquals(0, blockedUser2.getConnections().size());
    }

    @AfterEach
    void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
