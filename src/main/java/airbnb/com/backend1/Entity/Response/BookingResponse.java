package airbnb.com.backend1.Entity.Response;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import airbnb.com.backend1.Entity.Enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String bookingCode;
    private int guests;
    private Long days;
    private double totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
   
    private boolean isPaid;
   private Integer discountRate;
    private HomeResponse home;
    private UserResponse tenant;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;
    private Double priceAfterDiscount;
    private BookingStatus status;

    public BookingResponse(Long id, String bookingCode, int guests, Long days, double totalPrice, LocalDate checkInDate,  LocalDate checkOutDate, boolean isPaid, HomeResponse home, UserResponse tenant, LocalDate createDate, LocalDate updateDate, BookingStatus status) {
        this.id = id;
        this.bookingCode = bookingCode;
        this.guests = guests;
        this.days = days;
        this.totalPrice = totalPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = isPaid;
        this.home = home;
        this.tenant = tenant;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    
}
