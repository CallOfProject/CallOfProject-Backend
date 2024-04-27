package callofproject.dev.nosql.repository;

import callofproject.dev.nosql.entity.Notification;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Lazy
public interface INotificationRepository extends MongoRepository<Notification, UUID>
{
    void removeAllByNotificationOwnerId(UUID ownerId);

    Iterable<Notification> findAllByNotificationOwnerId(UUID ownerId);

    void removeAllByNotificationOwnerIdAndId(UUID ownerId, String id);


    Page<Notification> findByNotificationOwnerIdOrderByCreatedAtDesc(UUID ownerId, Pageable pageable);

    void deleteNotificationById(String id);


    @Query("{ 'notificationOwnerId' : ?0, 'isRead' : ?1 }")
    Iterable<Notification> findAllByNotificationOwnerIdAndRead(UUID ownerId, boolean read);

    @Query("{ 'notificationOwnerId' : ?0, 'read' : true }")
    Iterable<Notification> findAllReadNotificationsByNotificationOwnerId(UUID ownerId);

}
