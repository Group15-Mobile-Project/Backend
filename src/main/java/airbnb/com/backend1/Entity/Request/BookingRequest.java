package airbnb.com.backend1.Entity.Request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private int guests;
    private Long days;
    private double totalPrice;
    private Double priceAfterDiscount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    private Long homeId;
    

    public BookingRequest(int guests, Long days, double totalPrice, LocalDate checkInDate, LocalDate checkOutDate,   Long homeId) {
        this.guests = guests;
        this.days = days;
        this.totalPrice = totalPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.homeId = homeId;
    }

    

}
