package airbnb.com.backend1.Service.implementation;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import airbnb.com.backend1.Entity.Bookdate;
import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.HomeCategory;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.HomeRequest;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.HomeMapper;
import airbnb.com.backend1.Repository.BookdateRepos;
import airbnb.com.backend1.Repository.CityRepos;
import airbnb.com.backend1.Repository.CountryRepos;
import airbnb.com.backend1.Repository.HomeCategoryRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.HomeService;



@Service
public class HomeServiceIml implements HomeService {
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HomeMapper homeMapper;
    @Autowired
    HostRepos hostRepos;
    @Autowired
    CityRepos cityRepos;
    @Autowired
    CountryRepos countryRepos;
    @Autowired
    HomeCategoryRepos homeCategoryRepos;
    @Autowired
    BookdateRepos bookdateRepos;

    @Value("${GEOCODING_RESOURCE}")
    private String GEOCODING_RESOURCE;

    @Value("${API_KEY}")
	private String API_KEY;
  



    @Override
    public List<HomeResponse> getAll() {
        List<Home> homes = homeRepos.findAll();
        List<HomeResponse> responses = homes.stream().map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
        return responses;
    }
    @Override
    public List<HomeResponse> getAllByAuthUser() {
       Users authUser = getAuthUser();
       Host host = authUser.getHost();
       if(host == null) {
        throw new EntityNotFoundException("the host not found");
       }
       List<Home> homes = homeRepos.findByOwner(host);
       List<HomeResponse> responses = homes.stream().map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<HomeResponse> getAllByCity(String cityName) {
        Optional<City> entity = cityRepos.findByName(cityName);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the city not found");
        }
        List<Home> homes = homeRepos.findByCity(entity.get());
       List<HomeResponse> responses = homes.stream().map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<HomeResponse> getAllByCategory(Long categoryId) {
        Optional<HomeCategory> homeCategoryEntity = homeCategoryRepos.findById(categoryId);
        if(!homeCategoryEntity.isPresent()) {
            throw new EntityNotFoundException("the home category not found");
        }
        HomeCategory category = homeCategoryEntity.get();
        List<Home> homes = homeRepos.findByHomeCategory(category);
        List<HomeResponse> responses = homes.stream().map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<HomeResponse> getAllBySearch(String cityName, LocalDate startDate, LocalDate closeDate, Integer capacity) {
        LocalDate currentTime = LocalDate.now();
        if(startDate.isBefore(currentTime) ||closeDate.isBefore(currentTime)) {
            throw new BadResultException("startdate and closedate are not valid");
        }
        if(startDate.isAfter(closeDate)) {
            throw new BadResultException("startdate and closedate are not valid");
        }
        if(startDate.isEqual(closeDate)) {
            throw new BadResultException("startdate and closedate are not valid");
        }

        Optional<City> entity = cityRepos.findByName(cityName);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the city not found");
        }
        List<Home> homes = homeRepos.findByCity(entity.get());
       List<HomeResponse> responses = homes.stream().filter(hom -> hom.getCapacity() >= capacity).filter(hom -> checkDateOfHome(hom, startDate, closeDate)).filter(hom -> checkAvailabilityOfHome(hom, startDate, closeDate)).map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<HomeResponse> getAllByHost(Long hostId) {
       Optional<Host> entity = hostRepos.findById(hostId);
       if(!entity.isPresent()) {
        throw new EntityNotFoundException("the host not found");
    }
    List<Home> homes = homeRepos.findByOwner(entity.get());
   List<HomeResponse> responses = homes.stream().map(hom -> homeMapper.mapHomeToResponse(hom)).collect(Collectors.toList());
    return responses;
    }

    @Override
    public HomeResponse save(HomeRequest request) {

        Optional<Country> entityCountry = countryRepos.findByName(request.getCountry());
        if(!entityCountry.isPresent()) {
            throw new EntityNotFoundException("the country not found");
        }
        Country country = entityCountry.get();

        Optional<City> entityCity = cityRepos.findByName(request.getCity());
        City city;
        if(!entityCity.isPresent()) {
           city = new City(request.getCity(), country);
           cityRepos.save(city);
        } else {
            city = entityCity.get();
        }
       
        Optional<HomeCategory> homeCategoryEntity = homeCategoryRepos.findById(request.getHomeCategoryId());
        if(!homeCategoryEntity.isPresent()) {
            throw new EntityNotFoundException("the home category not found");
        }
        HomeCategory category = homeCategoryEntity.get();
        Users authuser = getAuthUser();
        Host host = authuser.getHost();
        if(host == null) {
            throw new EntityNotFoundException("the host not found or has not upgraded to host role");
        }
        
        boolean checkDate = checkDateOfCreateHome(request);
        if(checkDate == false) {
            throw new BadResultException("the closeDate and openDate are not valid");
        }

        request = geoLocationEncode(request);

        Home home = new Home(request.getTitle(), request.getPrice(), request.getAddress(),  city, country, request.getLatitude(), request.getLongtitude(), request.getZipcode(), request.getImgUrls(), request.getOpenBooking(), request.getCloseBooking(), request.getBeds(), request.getBedrooms(), request.getCapacity(), category, host);

        homeRepos.save(home);
        category.getHomes().add(home);
        homeCategoryRepos.save(category);
        host.getHomes().add(home);
        hostRepos.save(host);
        city.getHomes().add(home);
        cityRepos.save(city);

        return homeMapper.mapHomeToResponse(home);
    }

    @Override
    public HomeResponse getById(Long id) {
        Optional<Home> entity = homeRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the home not found");
        }
        return homeMapper.mapHomeToResponse(entity.get());
    }

    @Override
    public HomeResponse update(Long id, List<String> imgUrls, String title, Double price, Integer beds, Integer bedrooms, Integer capacity,  LocalDate closeBooking) {
        Optional<Home> entity = homeRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the home not found");
        }
        Home home = entity.get();
        Users authUser = getAuthUser();
        Host host = home.getOwner();
        if(authUser.getId() != host.getUser().getId()) {
            throw new BadResultException("unauthorized to update");
        }

        if(imgUrls != null) {
            home.setImgUrls(imgUrls);
        }
        if(title != null) {
            home.setTitle(title);
        }
        if(price != null && price > 0) {
            home.setPrice(price);
        }
        if(beds != null && beds > 0) {
            home.setBeds(beds);
        }
        if(bedrooms != null && bedrooms > 0) {
            home.setBedrooms(bedrooms);
        }
        if(capacity != null && capacity > 0) {
            home.setCapacity(capacity);
        }

        LocalDate currentTime = LocalDate.now();
        if(closeBooking != null && closeBooking.isAfter(currentTime) && closeBooking.isAfter(home.getCloseBooking())) {
            home.setCloseBooking(closeBooking);
        }

        homeRepos.save(home);
        return homeMapper.mapHomeToResponse(home);

    }

    private Users isCheck(Optional<Users> entity) {
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the user not found");
    }
    private Users getAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        return user;
    }
    
    private boolean checkDateOfHome(Home home, LocalDate checkinDate, LocalDate checkOutDate) {
      

        if(checkinDate.isBefore(home.getOpenBooking())) {
            return false;
        }
        if(checkOutDate.isAfter(home.getCloseBooking())) {
            return false;
        }
        return true;
    }

    public boolean checkAvailabilityOfHome(Home home, LocalDate startDate, LocalDate closeDate) {
      boolean isAvaiblable = true;
      LocalDate checkInDate = startDate;
      LocalDate checkOutDate = closeDate.minusDays(1);
    if(!startDate.isEqual(closeDate.minusDays(1))) {
        while(!checkInDate.isAfter(checkOutDate)) {
            Optional<Bookdate> entity = bookdateRepos.findByDateAndHome(checkInDate, home);
            if(entity.isPresent()) {
                return false;
            } else {
                checkInDate = checkInDate.plusDays(1);
            }
        }
    } else {
        Optional<Bookdate> entity = bookdateRepos.findByDateAndHome(startDate, home);
        if(entity.isPresent()) {
            return false;
        } else {
            return true;
        }
    }
        return isAvaiblable;
    }


    private boolean checkDateOfCreateHome(HomeRequest request) {
        LocalDate currentTime = LocalDate.now();
        if(request.getOpenBooking().isEqual(currentTime) && request.getCloseBooking().isAfter(currentTime)) {
            return true;
        }
        if(request.getOpenBooking().isAfter(currentTime) && request.getCloseBooking().isAfter(request.getOpenBooking())) {
            return true;
        }
        return false;
    }
   

    private HomeRequest geoLocationEncode(HomeRequest request) {
       try {
        HttpClient httpClient = HttpClient.newHttpClient();
        String address = request.getAddress() + " ," + request.getZipcode() + " " + request.getCity()  + " ," +  request.getCountry();
        String addressEncode = URLEncoder.encode(address, "UTF-8");
        String uriRequest = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + addressEncode;

        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(uriRequest)).timeout(Duration.ofMillis(2000)).build();

        String res = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(res);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode resJson = mapper.readTree(res);
        JsonNode items = resJson.get("items");
        if(items != null) {
            for(JsonNode item : items) {
                JsonNode location = item.get("position");
                String latitude = location.get("lat") != null ? location.get("lat").asText() : null;
                String longtitude = location.get("lng") != null ? location.get("lng").asText() : null;
                System.out.println("longtitude: " + longtitude + " latitude: " + latitude);
                if(latitude != null) {
                    request.setLatitude(latitude);
                }
                if(longtitude != null) {
                    request.setLongtitude(longtitude);
                }
            }
        }


        return request;
       } catch (InterruptedException ex) {
        throw new BadResultException(ex.getMessage());
       
       } catch (IOException ex) {
        throw new BadResultException(ex.getMessage());
       }
        
    }

    
}
