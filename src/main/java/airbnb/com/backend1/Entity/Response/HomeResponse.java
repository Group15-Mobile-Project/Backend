package airbnb.com.backend1.Entity.Response;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Entity.HomeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeResponse {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openBooking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeBooking;
    private String title;
    private Double price;
    private String address;
    private City city;
    private Country country;
    private String latitude;
    private String longtitude;
    private String zipcode;
    private List<String> imgUrls;
    private int beds;
    private int bedrooms;
    private int capacity;
    private Double rating;
    private int reviewNums;
    private HomeCategory homeCategory;
    private HostResponse owner;
    private DiscountResponse discount;
    public HomeResponse(Long id, LocalDate openBooking, LocalDate closeBooking, String title, Double price,
            String address, City city, Country country, String latitude, String longtitude, String zipcode,
            List<String> imgUrls, int beds, int bedrooms, int capacity, int reviewNums, HomeCategory homeCategory,
            HostResponse owner) {
        this.id = id;
        this.openBooking = openBooking;
        this.closeBooking = closeBooking;
        this.title = title;
        this.price = price;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.zipcode = zipcode;
        this.imgUrls = imgUrls;
        this.beds = beds;
        this.bedrooms = bedrooms;
        this.capacity = capacity;
        this.reviewNums = reviewNums;
        this.homeCategory = homeCategory;
        this.owner = owner;
    }

    
}
