package airbnb.com.backend1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Country;

@Repository
public interface CountryRepos extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
