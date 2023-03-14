package airbnb.com.backend1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.HomeCategory;

@Repository
public interface HomeCategoryRepos extends JpaRepository<HomeCategory, Long> {
    Optional<HomeCategory> findByName(String name);
}
