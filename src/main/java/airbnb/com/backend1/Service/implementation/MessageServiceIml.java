package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Chat;
import airbnb.com.backend1.Entity.Message;
import airbnb.com.backend1.Entity.Participant;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.MessageRequest;
import airbnb.com.backend1.Entity.Response.MessageResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.MessageMapper;
import airbnb.com.backend1.Repository.ChatRepos;
import airbnb.com.backend1.Repository.MessageRepos;
import airbnb.com.backend1.Repository.ParticipantRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.MessageService;

@Service
public class MessageServiceIml implements MessageService {
    @Autowired
    ChatRepos chatRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    ParticipantRepos participantRepos;
    @Autowired
    MessageMapper mapper;
    @Autowired
    MessageRepos messageRepos;

    @Override
    public MessageResponse add(MessageRequest req) {
        Chat chat = getChat(req.getChatId());
        Users authUser = getAuthUser();
        Optional<Participant> entity = participantRepos.findByChatAndUser(chat, authUser);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the participant not found");
        }
       Participant participant = entity.get();
       Message mess = new Message(req.getContent(), chat, participant);
       messageRepos.save(mess);

       chat.getMessages().add(mess);
       chat.setLastMessage(mess);
       participant.getMessages().add(mess);
       participant.setRead(true);

       List<Participant> participants = chat.getParticipants();

       Participant participant2 = participants.get(0).getId() != participant.getId() ? participants.get(0) : participants.get(1);
       participant2.setRead(false);


        chatRepos.save(chat);
        participantRepos.save(participant);
        participantRepos.save(participant2);

        return mapper.mapMessageToResponse(mess);

    }

    @Override
    public void deleteMessage(Long id) {
        Users authUser = getAuthUser();
        Optional<Message> entity = messageRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the message not found");
        }
        Message mess = entity.get();
        Participant participant = mess.getParticipant();
        Chat chat = mess.getChat();
        if(participant.getUser().getId() != authUser.getId()) {
            throw new BadResultException("unAuthorized to delete the message");
        }
        if(chat.getLastMessage().getId() == mess.getId()) {
            chat.setLastMessage(null);
            chatRepos.save(chat);
        }
        messageRepos.delete(mess);
    }

    @Override
    public List<MessageResponse> getAllByChat(Long chatId) {
        Chat chat = getChat(chatId);
        List<Message> mess = messageRepos.findByChat(chat);
        List<MessageResponse> res = mess.stream().map(mes -> mapper.mapMessageToResponse(mes)).collect(Collectors.toList());
        res.sort((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()));
        return res;
    }

    @Override
    public List<MessageResponse> getAllByAuthserAndReceiver(Long receiverId) {
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
        Chat chat = chatsFilter.get(0);
        List<Message> mess = messageRepos.findByChat(chat);
        List<MessageResponse> res = mess.stream().map(mes -> mapper.mapMessageToResponse(mes)).collect(Collectors.toList());
        res.sort((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()));
        return res;
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
    private Chat getChat(Long chatId) {
        Optional<Chat> entity = chatRepos.findById(chatId);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the chat not found");
    }
}
