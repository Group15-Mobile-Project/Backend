package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Discount;

@Repository
public interface DiscountRepos extends JpaRepository<Discount, Long> {
    @Query(value = "select discount from Discount discount LEFT JOIN discount.home home where home.id = :homeId")
    Optional<Discount> findByHome(Long homeId);
}
