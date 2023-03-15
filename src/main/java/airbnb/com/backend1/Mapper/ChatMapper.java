package airbnb.com.backend1.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Chat;
import airbnb.com.backend1.Entity.Message;
import airbnb.com.backend1.Entity.Participant;
import airbnb.com.backend1.Entity.Response.ChatResponse;
import airbnb.com.backend1.Entity.Response.ParticipantResponse;
import airbnb.com.backend1.Repository.ParticipantRepos;

@Component
public class ChatMapper {
    @Autowired
    ParticipantMapper mapper;
    @Autowired
    ParticipantRepos participantRepos;
    @Autowired
    MessageMapper messageMapper;

    public ChatResponse mapChatToResponse(Chat chat) {
        List<Participant> participants = participantRepos.findByChat(chat);
        List<ParticipantResponse> participantResponses = participants.stream().map(part -> mapper.mapParticipantToResponse(part)).collect(Collectors.toList());
        ChatResponse res = new ChatResponse(chat.getId(), participantResponses, chat.getDateCreated(), chat.getDateUpdated());
        if(chat.getLastMessage() != null) {
            res.setLastMessage(messageMapper.mapMessageToResponse(chat.getLastMessage()));
        }
        return res;
    }
}
