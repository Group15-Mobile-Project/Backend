package airbnb.com.backend1.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.HomeRequest;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Service.HomeService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/homes")
public class HomeController {
    @Autowired
    HomeService homeService;

    @GetMapping("/all")
    public ResponseEntity<List<HomeResponse>> getAll() {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<HomeResponse>> getAllByCity(@PathVariable String cityName) {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAllByCity(cityName), HttpStatus.OK);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<HomeResponse>> getAllByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAllByCategory(categoryId), HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<HomeResponse>> getAllBySearchQuery(@RequestParam(required = false) String city, @RequestParam(required = false) LocalDate startDay, @RequestParam(required = false) LocalDate closeDay, @RequestParam(required = false) Integer capacity ) {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAllBySearch(city, startDay, closeDay, capacity), HttpStatus.OK);
    }
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<HomeResponse>> getAllByHost(@PathVariable Long hostId) {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAllByHost(hostId), HttpStatus.OK);
    }
    @GetMapping("/authUser")
    public ResponseEntity<List<HomeResponse>> getAllByAuthUser() {
        return new ResponseEntity<List<HomeResponse>>(homeService.getAllByAuthUser(), HttpStatus.OK);
    }
    @PostMapping("/home/")
    public ResponseEntity<HomeResponse> saveHome(@RequestBody @Valid HomeRequest homeRequest) {
        return new ResponseEntity<HomeResponse>(homeService.save(homeRequest), HttpStatus.CREATED);
    }
    @PutMapping("/home/{id}")
    public ResponseEntity<HomeResponse> updateHome(@PathVariable long id, @RequestParam(required = false) List<String> imgUrls, @RequestParam(required = false) String title, @RequestParam(required = false) Double price, @RequestParam(required = false) Integer beds, @RequestParam(required = false) Integer bedrooms, @RequestParam(required = false) Integer capacity, @RequestParam(required = false) LocalDate closeBooking) {
        return new ResponseEntity<HomeResponse>(homeService.update(id, imgUrls, title, price, beds, bedrooms, capacity, closeBooking), HttpStatus.OK);
    }
    @GetMapping("/home/{id}")
    public ResponseEntity<HomeResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<HomeResponse>(homeService.getById(id), HttpStatus.OK);
    }
}
