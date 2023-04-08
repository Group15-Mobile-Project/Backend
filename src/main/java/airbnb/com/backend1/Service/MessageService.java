package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Request.MessageRequest;
import airbnb.com.backend1.Entity.Response.MessageResponse;

public interface MessageService {
 List<MessageResponse> getAllByChat(Long chatId);
 List<MessageResponse> getAllByAuthserAndReceiver(Long receiverId);
 MessageResponse add(MessageRequest req);
 void deleteMessage(Long id);   
}
