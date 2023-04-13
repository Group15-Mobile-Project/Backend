package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Service.CountryService;

@CrossOrigin
@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    CountryService countryService;

    @GetMapping("/country/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id) {
        return new ResponseEntity<Country>(countryService.getCountryById(id), HttpStatus.OK);
    }
    @GetMapping("/country/name/{name}")
    public ResponseEntity<Country> getCountryByName(@PathVariable String name) {
        return new ResponseEntity<Country>(countryService.getCountryByName(name), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/country/name/{name}")
    public ResponseEntity<Country> saveCountryByName(@PathVariable String name) {
        return new ResponseEntity<Country>(countryService.save(name), HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Country>> getAll() {
        return new ResponseEntity<List<Country>>(countryService.getAll(), HttpStatus.OK);
    }
}
