package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Users;



@Repository
public interface UserRepos extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    List<Users> findByUsernameContaining(String username);
}