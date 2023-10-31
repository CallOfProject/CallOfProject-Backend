package callofproject.dev.authentication.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "session")
public class Session
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID session_id;
    @Column
    private String token;
    @Column
    private LocalDate expireDate;
    @Column
    @CreationTimestamp
    private LocalDate creationDate;
}
