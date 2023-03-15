package airbnb.com.backend1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Chat;
import airbnb.com.backend1.Entity.Message;
import airbnb.com.backend1.Entity.Response.MessageResponse;

@Repository
public interface MessageRepos extends JpaRepository<Message, Long>{
    List<Message> findByChat(Chat chat);
}
