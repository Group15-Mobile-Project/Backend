package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Repository.CityRepos;
import airbnb.com.backend1.Service.CityService;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    CityService cityService;

    @GetMapping("/query/{query}")
    public ResponseEntity<List<City>> getCitiesByQuery(@PathVariable String query) {
        return new ResponseEntity(cityService.getAllByQuerySearch(query), HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<City>> getAll() {
        return new ResponseEntity(cityService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/country/{countryName}")
    public ResponseEntity<List<City>> getAllByCountry(@PathVariable String countryName) {
        return new ResponseEntity(cityService.getAllByCountry(countryName), HttpStatus.OK);
    }

    @GetMapping("/city/{id}")
    public ResponseEntity<City> getById(@PathVariable Long id) {
        return new ResponseEntity(cityService.getById(id), HttpStatus.OK);
    }
    @GetMapping("/city/name/{cityName}")
    public ResponseEntity<City> getAllByCityName(@PathVariable String cityName) {
        return new ResponseEntity(cityService.getByName(cityName), HttpStatus.OK);
    }
}
