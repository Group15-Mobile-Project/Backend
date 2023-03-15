package airbnb.com.backend1.Entity;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Participant")
@Table(name = "participant", uniqueConstraints = {@UniqueConstraint(columnNames = {"chat_id", "user_id"})})
@Getter
@Setter
public class Participant {
    @Id
    @SequenceGenerator(
        name = "participant_sequence",
        allocationSize = 1,
        sequenceName = "participant_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "participant_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @JsonIgnore
    @OneToMany(mappedBy = "participant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    public Participant(Chat chat, Users user) {
        this.chat = chat;
        this.user = user;
        this.isRead = true;
    }

    
}
