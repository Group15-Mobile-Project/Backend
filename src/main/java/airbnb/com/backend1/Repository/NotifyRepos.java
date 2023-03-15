package airbnb.com.backend1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Notify;
import airbnb.com.backend1.Entity.Users;

@Repository
public interface NotifyRepos extends JpaRepository<Notify, Long>{
    List<Notify> findByTenant(Users tenant);
    List<Notify> findByHost(Host host);
}
