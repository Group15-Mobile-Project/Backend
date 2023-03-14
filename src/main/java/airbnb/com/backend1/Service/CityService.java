package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;

public interface CityService {
    List<City> getAll();
    List<City> getAllByCountry(String CountryName);
    List<City> getAllByQuerySearch(String query);
    City saveCity(String cityName, Country country);
    City getByName(String cityName);
    City getById(Long id);
}
