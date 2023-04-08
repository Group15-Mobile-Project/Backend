package airbnb.com.backend1.Entity.Response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HostStatistics {
    private HostResponse host;
    private double earnings;
    private int bookings;
    private double rating;
    private int homes;
    private int reviews;
    public HostStatistics(HostResponse host, double earnings, int bookings, int homes, int reviews) {
        this.host = host;
        this.earnings = earnings;
        this.bookings = bookings;
        this.homes = homes;
        this.reviews = reviews;
    }

    
}
