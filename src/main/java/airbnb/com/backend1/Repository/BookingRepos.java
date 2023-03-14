package airbnb.com.backend1.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Users;

@Repository
public interface BookingRepos extends JpaRepository<Booking, Long> {
    List<Booking> findByTenant(Users tenant);
    List<Booking> findByHome(Home home);
    @Query(value = "select b from Booking b LEFT JOIN b.home home where home.owner.id = :hostId")
    List<Booking> findByHost(Long hostId);
}
