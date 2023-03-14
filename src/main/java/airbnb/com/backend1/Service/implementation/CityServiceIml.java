package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Repository.CityRepos;
import airbnb.com.backend1.Repository.CountryRepos;
import airbnb.com.backend1.Service.CityService;
import airbnb.com.backend1.Service.CountryService;

@Service
public class CityServiceIml  implements CityService{
    @Autowired
    CityRepos cityRepos;
    @Autowired
    CountryService countryService;
    @Autowired
    CountryRepos countryRepos;
    @Override
    public List<City> getAll() {
       return cityRepos.findAll();
    }
    @Override
    public List<City> getAllByCountry(String CountryName) {
    //    Country country = countryService.getCountryByName(CountryName);
    Optional<Country> entity = countryRepos.findByName(CountryName);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the country not found");
        }
       Country country = entity.get();
       return cityRepos.findByCountry(country);
    }
    @Override
    public List<City> getAllByQuerySearch(String query) {
        if(!query.isBlank()) {
            return cityRepos.findByCityOrCountry(query.toLowerCase());
        }
      throw new BadResultException("the query must not be blank");
    }
    @Override
    public City saveCity(String cityName, Country country) {
        Optional<City> entity = cityRepos.findByName(cityName);
        if(entity.isPresent()) {
            throw new EntityExistingException("the city exist");
        }
        City city = new City(cityName, country);
        cityRepos.save(city);
        country.getCities().add(city);
        countryRepos.save(country);

        return city;
    }
    @Override
    public City getByName(String cityName) {
        Optional<City> entity = cityRepos.findByName(cityName);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the city not found");
        }
        return entity.get();
    }
    @Override
    public City getById(Long id) {
        Optional<City> entity = cityRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the city not found");
        }
        return entity.get();
    }
    
}
