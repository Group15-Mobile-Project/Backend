package airbnb.com.backend1.Entity.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private Long id;
    private MessageResponse lastMessage;

    private List<ParticipantResponse> participants = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateCreated;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateUpdated;

    public ChatResponse(Long id,  List<ParticipantResponse> participants, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.participants = participants;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    
}
