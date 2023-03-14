package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Repository.CountryRepos;
import airbnb.com.backend1.Service.CountryService;

@Service
public class CountryServiceIml implements CountryService {
    @Autowired
    CountryRepos countryRepos;

    @Override
    public List<Country> getAll() {
        return countryRepos.findAll();
    }

    @Override
    public Country getCountryById(Long id) {
        Optional<Country> entity = countryRepos.findById(id);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the country not found");
    }

    @Override
    public Country getCountryByName(String name) {
        Optional<Country> entity = countryRepos.findByName(name);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the country not found");
    }

    @Override
    public Country save(String name) {
        Optional<Country> entity = countryRepos.findByName(name);
        if(entity.isPresent()) {
            throw new EntityExistingException("the country exists");
        }
        Country country = new Country(name);
        return countryRepos.save(country);
    }
    
}
