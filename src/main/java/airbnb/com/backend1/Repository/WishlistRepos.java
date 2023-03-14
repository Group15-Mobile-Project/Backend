package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Wishlist;

public interface WishlistRepos extends JpaRepository<Wishlist, Long> {
    
    @Query(value = "select w from Wishlist w LEFT JOIN w.user user WHERE user.id = :userId")
    List<Wishlist> findByUser(Long userId);
    @Query(value = "select w from Wishlist w LEFT JOIN w.home home WHERE home.id = :homeId")
    List<Wishlist> findByHome(Long homeId);
    Optional<Wishlist> findByHomeAndUser(Home home, Users user);
}
