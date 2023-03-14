package airbnb.com.backend1.Entity.Request;
import airbnb.com.backend1.Entity.City;
import airbnb.com.backend1.Entity.Country;
import airbnb.com.backend1.Entity.HomeCategory;
import airbnb.com.backend1.Entity.Response.HostResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HomeRequest {
   
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openBooking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeBooking;
    private String title;
    private Double price;
    private String address;
    private String city;
    private String country;
    private String zipcode;
    private List<String> imgUrls;
    private int beds;
    private int bedrooms;
    private int capacity;
    private Long homeCategoryId;
    private String latitude;
    private String longtitude;
    public HomeRequest(LocalDate openBooking, LocalDate closeBooking, String title, Double price, String address,
            String city, String country, String zipcode, List<String> imgUrls, int beds, int bedrooms, int capacity,
            Long homeCategoryId) {
        this.openBooking = openBooking;
        this.closeBooking = closeBooking;
        this.title = title;
        this.price = price;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
        this.imgUrls = imgUrls;
        this.beds = beds;
        this.bedrooms = bedrooms;
        this.capacity = capacity;
        this.homeCategoryId = homeCategoryId;
    }

    

}
