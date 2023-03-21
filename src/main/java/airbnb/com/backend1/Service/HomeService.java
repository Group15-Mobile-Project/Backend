package airbnb.com.backend1.Service;

import java.time.LocalDate;
import java.util.List;

import airbnb.com.backend1.Entity.Request.HomeRequest;
import airbnb.com.backend1.Entity.Response.HomeResponse;

public interface HomeService {
    List<HomeResponse> getAll();
    List<HomeResponse> getAllByCity(String cityName);
    List<HomeResponse> getAllByCategory(Long categoryId);
    List<HomeResponse> getAllBySearch(String cityName, LocalDate startDay, LocalDate closeDate, Integer capacity);
    List<HomeResponse> getAllByHost(Long hostId);
    List<HomeResponse> getAllByAuthUser();
    HomeResponse save(HomeRequest homeRequest);
    HomeResponse update(Long id, List<String> imgUrls, String title, Double price, Integer beds, Integer bedrooms, Integer capacity,  LocalDate closeBooking);
    HomeResponse getById(Long id);
}
