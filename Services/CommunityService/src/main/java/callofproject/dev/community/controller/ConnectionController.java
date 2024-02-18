package callofproject.dev.community.controller;

import callofproject.dev.community.service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/community/personal-connection")
public class ConnectionController
{
    private final ConnectionService m_connectionService;

    public ConnectionController(ConnectionService connectionService)
    {
        m_connectionService = connectionService;
    }

    @PostMapping("/send/connection-request")
    public ResponseEntity<?> sendConnectionRequest(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        return subscribe(() -> ok(m_connectionService.sendConnectionRequest(userId, friendId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/answer/connection-request")
    public ResponseEntity<?> acceptConnectionRequest(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId, @RequestParam("answer") boolean answer)
    {
        return subscribe(() -> ok(m_connectionService.answerConnectionRequest(userId, friendId, answer)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/remove/connection")
    public ResponseEntity<?> removeConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        return subscribe(() -> ok(m_connectionService.removeConnection(userId, friendId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/block/connection")
    public ResponseEntity<?> blockConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        return subscribe(() -> ok(m_connectionService.blockConnection(userId, friendId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @PostMapping("/unblock/connection")
    public ResponseEntity<?> unblockConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        return subscribe(() -> ok(m_connectionService.unblockConnection(userId, friendId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/get/connections")
    public ResponseEntity<?> getConnections(@RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_connectionService.getConnectionsByUserId(userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/get/connection-requests")
    public ResponseEntity<?> getConnectionRequests(@RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_connectionService.getConnectionRequestsByUserId(userId)), ex -> internalServerError().body(ex.getMessage()));
    }

    @GetMapping("/get/blocked-connections")
    public ResponseEntity<?> getBlockedConnections(@RequestParam("user_id") UUID userId)
    {
        return subscribe(() -> ok(m_connectionService.getBlockedConnectionsByUserId(userId)), ex -> internalServerError().body(ex.getMessage()));
    }
}