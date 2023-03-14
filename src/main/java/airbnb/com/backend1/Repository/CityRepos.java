package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;

@Repository
public interface CityRepos extends JpaRepository<City, Long> {
    List<City> findByNameContaining(String name);
    @Query(value = "select city from City city LEFT JOIN city.country country where city.name LIKE CONCAT('%', :query, '%') OR country.name LIKE CONCAT('%', :query, '%')")
    List<City> findByCityOrCountry(String query);
    List<City> findByCountry(Country country);
    Optional<City> findByName(String name);
}
