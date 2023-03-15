package airbnb.com.backend1.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Message;
import airbnb.com.backend1.Entity.Response.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
public class MessageMapper {
        @Autowired
        ParticipantMapper mapper;

        public MessageResponse mapMessageToResponse(Message mes) {
            return new MessageResponse(mes.getId(), mes.getContent(), mes.getChat().getId(), mapper.mapParticipantToResponse(mes.getParticipant()), mes.getDateCreated(), mes.getDateUpdated());
            
        }
}
