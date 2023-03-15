package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Chat;
import airbnb.com.backend1.Entity.Participant;
import airbnb.com.backend1.Entity.Users;

@Repository
public interface ParticipantRepos extends JpaRepository<Participant, Long> {
    List<Participant> findByChat(Chat chat);
    Optional<Participant> findByChatAndUser(Chat chat, Users user);
}
