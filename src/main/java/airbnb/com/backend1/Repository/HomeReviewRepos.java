package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.HomeReview;
import airbnb.com.backend1.Entity.Users;

@Repository
public interface HomeReviewRepos extends JpaRepository<HomeReview, Long> {
   List<HomeReview> findByHome(Home home);
   Optional<HomeReview> findByHomeAndUser(Home home, Users user);
}
