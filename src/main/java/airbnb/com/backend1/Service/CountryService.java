package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Country;

public interface CountryService {
    List<Country> getAll();
    Country save(String name);
    Country getCountryById(Long id);
    Country getCountryByName(String name);
}
