package airbnb.com.backend1.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Bookdate;
import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;

@Repository
public interface BookdateRepos extends JpaRepository<Bookdate, Long> {
    List<Bookdate> findByBooking(Booking booking);
    List<Bookdate> findByHome(Home home);
    Optional<Bookdate> findByDateAndHome(LocalDate date, Home home);
    @Query(value = "select bookdate from Bookdate bookdate LEFT JOIN bookdate.home home where home.owner.id = :hostId")
    List<Bookdate> findByHostId(Long hostId);
}
