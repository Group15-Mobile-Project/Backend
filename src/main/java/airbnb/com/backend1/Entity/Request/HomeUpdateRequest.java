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
public class HomeUpdateRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openBooking;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeBooking;
    private String title;
    private Double price;
    private List<String> imgUrls;
    private int beds;
    private int bedrooms;
    private int capacity;
}
