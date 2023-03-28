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
public class CountDiscoutResponse {
    private Double discountPrice;
    private Double priceAfterDiscount;
    private Double totalPrice;
    private Long homeId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long days;
}
