package callofproject.dev.community.controller;

import callofproject.dev.community.service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/connection")
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
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/accept/connection-request")
    public ResponseEntity<?> acceptConnectionRequest(@RequestParam("request_id") UUID requestId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/reject/connection-request")
    public ResponseEntity<?> rejectConnectionRequest(@RequestParam("request_id") UUID requestId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/remove/connection")
    public ResponseEntity<?> removeConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/block/connection")
    public ResponseEntity<?> blockConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @PostMapping("/unblock/connection")
    public ResponseEntity<?> unblockConnection(@RequestParam("user_id") UUID userId, @RequestParam("friend_id") UUID friendId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/connections")
    public ResponseEntity<?> getConnections(@RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/connection-requests")
    public ResponseEntity<?> getConnectionRequests(@RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/get/blocked-connections")
    public ResponseEntity<?> getBlockedConnections(@RequestParam("user_id") UUID userId)
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}
