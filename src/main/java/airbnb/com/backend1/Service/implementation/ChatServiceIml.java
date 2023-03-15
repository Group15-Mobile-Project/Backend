package airbnb.com.backend1.Service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Chat;
import airbnb.com.backend1.Entity.Participant;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.ChatResponse;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.ChatMapper;
import airbnb.com.backend1.Repository.ChatRepos;
import airbnb.com.backend1.Repository.ParticipantRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.ChatService;

@Service
public class ChatServiceIml implements ChatService {
    @Autowired
    ChatRepos chatRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    ParticipantRepos participantRepos;
    @Autowired
    ChatMapper chatMapper;


    @Override
    public List<ChatResponse> getAllByAuthUser() {
        Users authUser = getAuthUser();
        List<Chat> chats = chatRepos.findByAuthUser(authUser.getId());
        List<ChatResponse> res = chats.stream().map(chat -> chatMapper.mapChatToResponse(chat)).collect(Collectors.toList());
        return res;
    }

    @Override
    public ChatResponse getByAuthUserAndReceiver(Long receiverId) {
        Users authUser = getAuthUser();
        Users receiver = getReceiver(receiverId);
        List<Chat> chats = chatRepos.findByAuthUser(authUser.getId());
        List<Chat> chatsFilter = chats.stream().filter(chat -> {
            Optional<Participant> entityParti = participantRepos.findByChatAndUser(chat, receiver);
            if(entityParti.isPresent()) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        System.out.println(chatsFilter.size() + " : chatfilter size");
        if(!chatsFilter.isEmpty() && chatsFilter.size() > 0) {
            Chat chat = chatsFilter.get(0);
            System.out.println(chat);
            ChatResponse res = chatMapper.mapChatToResponse(chat);
            return res;
        } else {
            Chat chat = new Chat();
            Participant participant1 = new Participant(chat, authUser);
            Participant participant2 = new Participant(chat, receiver);
            chat.getParticipants().add(participant1);
            chat.getParticipants().add(participant2);
            
            authUser.getParticipants().add(participant1);
            receiver.getParticipants().add(participant2);
            chatRepos.save(chat);
            userRepos.save(authUser);
            userRepos.save(receiver);

            return chatMapper.mapChatToResponse(chat);
            
        }

    }
    @Override
    public ChatResponse UpdateReadStatus(Long id) {
        Optional<Chat> entity = chatRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the chat not found");
        }
        Chat chat = entity.get();
        List<Participant> participants = participantRepos.findByChat(chat);
        
        participants.stream().forEach(parti -> {
            parti.setRead(true);
            participantRepos.save(parti);
        });
        // chat.setParticipants(participants);

        chatRepos.save(chat);
        return chatMapper.mapChatToResponse(chat);
       
    }

    @Override
    public ChatResponse getById(Long Id) {
        
        Optional<Chat> entity = chatRepos.findById(Id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the chat not found");
        }
        return chatMapper.mapChatToResponse(entity.get());


    }
    private Users isCheck(Optional<Users> entity) {
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the user not found");
    }
    private Users getAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        return user;
    }
    private Users getReceiver(Long receiverId) {
        
        Optional<Users> entity = userRepos.findById(receiverId);
        Users user = isCheck(entity);
        return user;
    }
    
}
