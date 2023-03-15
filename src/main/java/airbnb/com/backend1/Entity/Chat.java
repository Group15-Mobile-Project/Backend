package airbnb.com.backend1.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Chat")
@Table(name = "chat")
@Getter
@Setter
public class Chat {
    @Id
    @SequenceGenerator(
        name = "chat_sequence",
        allocationSize = 1,
        sequenceName = "chat_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "chat_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;


    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "last_message_id", referencedColumnName = "id")
    private Message lastMessage;

    @JsonIgnore
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

   
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Participant> participants= new ArrayList<>();

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "date_updated", nullable = false)
    private LocalDateTime dateUpdated;

    public Chat( List<Participant> participants) {
        
        this.participants = participants;
    }

    

    
}
