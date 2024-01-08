package callofproject.dev.service.notification.controller;

import callofproject.dev.service.notification.config.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("api/notification")
public class NotificationController
{
    private final NotificationService m_notificationService;

    /**
     * Constructor.
     *
     * @param notificationService represent the notification service.
     */
    public NotificationController(NotificationService notificationService)
    {
        m_notificationService = notificationService;
    }

    /**
     * Find all notifications by notification owner id and sort created at.
     *
     * @param userId represent the user id.
     * @param page   represent the page.
     * @return ResponseEntity.
     */
    @GetMapping("find/all/sort/created-at")
    public ResponseEntity<Object> findAllNotificationsByNotificationOwnerIdAndSortCreatedAt(@RequestParam("uid") UUID userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_notificationService.findAllNotificationsByNotificationOwnerIdAndSortCreatedAt(userId, page)),
                msg -> internalServerError().body(msg.getMessage()));
    }
}
