package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Host;

@Repository
public interface HostRepos extends JpaRepository<Host, Long> {
    @Query(value = "select host from Host host LEFT JOIN host.user user where user.id = :id")
    Optional<Host> findByUserId(Long id);
    @Query(value = "select host from Host host LEFT JOIN host.user user where user.username LIKE CONCAT('%', :username, '%')")
    List<Host> findByUsername(String username);
}
