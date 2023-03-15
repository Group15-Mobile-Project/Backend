package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Response.ChatResponse;

public interface ChatService {
    List<ChatResponse> getAllByAuthUser();
    ChatResponse getByAuthUserAndReceiver(Long receiverId);
    ChatResponse getById(Long Id);
    ChatResponse UpdateReadStatus(Long id);
}
