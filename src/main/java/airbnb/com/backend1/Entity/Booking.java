package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Booking")
@Table(name = "booking")
@Getter
@Setter
public class Booking {
    @Id
    @SequenceGenerator(
        name = "booking_sequence",
        allocationSize = 1,
        sequenceName = "booking_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "booking_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Min(value = 1, message = "number of guests must be atleast 1")
    @Column(name = "guests", nullable = false)
    private int guests;
    @Min(value = 1, message = "number of days must be atleast 1")
    @Column(name = "days", nullable = false)
    private Long days;
    
    @Min(value = 0, message = "total price must be atleast 0")
    @Column(name = "total_price", nullable = false)
    private double totalPrice;
    
    @Min(value = 0, message = "total price after discount must be atleast 0")
    @Column(name = "price_after_discount")
    private Double PriceAfterDiscount;

    @Column(name = "check_in_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @Column(name = "check_out_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;


    @JsonIgnore
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HomeReview homeReview;

    @JsonIgnore
    @OneToOne(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TenantReview tenantReview;


    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;
    
    // @Min(value = 1, message = "discount rate must be higher than 0")
    // @Max(value = 100, message = "discount rate must be smaller than 100")
    // @Column(name = "discount_rate",  nullable = false)
    // private Integer discountRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
    
    @Column(name = "booking_code", nullable = false)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "home_id",
        referencedColumnName = "id"
    )
    private Home home;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "tenant_id",
        referencedColumnName = "id"
    )
    private Users tenant;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bookdate> bookdates = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notify> notifies = new ArrayList<>();

    public Booking( int guests, Long days, double totalPrice, LocalDate checkInDate,  LocalDate checkOutDate, boolean isPaid,  String bookingCode, Home home, Users tenant) {
        this.guests = guests;
        this.days = days;
        this.totalPrice = totalPrice;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = isPaid;
        this.bookingCode = bookingCode;
        this.home = home;
        this.tenant = tenant;
    }



    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean getIsPaid() {
        return this.isPaid;
    }



    @Override
    public String toString() {
        return "Booking [id=" + id + ", guests=" + guests + ", days=" + days + ", totalPrice=" + totalPrice
                + ", PriceAfterDiscount=" + PriceAfterDiscount + ", checkInDate=" + checkInDate + ", checkOutDate="
                + checkOutDate + ", createDate=" + createDate + ", updateDate=" + updateDate + ", isPaid=" + isPaid
                + ", bookingCode=" + bookingCode +  "]";
    }
    
    
}
