package airbnb.com.backend1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Chat;

@Repository
public interface ChatRepos extends JpaRepository<Chat, Long> {
    @Query(value = "select chat from Chat chat LEFT JOIN chat.participants p where p.user.id = :userId ORDER BY chat.dateCreated DESC")
    List<Chat> findByAuthUser(Long userId);
}
