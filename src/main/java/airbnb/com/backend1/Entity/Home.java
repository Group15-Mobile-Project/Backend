package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Validation.IsValidDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "home")
@Table(name = "home")
@Getter
@Setter
@IsValidDate
public class Home {
    @Id
    @SequenceGenerator(
        name = "home_sequence",
        allocationSize = 1,
        sequenceName = "home_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "home_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    
    @NotBlank(message = "title cannot be blank")
    @Column(name = "title",  nullable = false)
    private String title;

    @Min(value = 0, message = "price must be higher than 0")
    @Column(name = "price",  nullable = false)
    private Double price;

    @NotBlank(message = "address cannot be blank")
    @Column(name = "address",  nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "city_id",
        referencedColumnName = "id"
    )
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "country_id",
        referencedColumnName = "id"
    )
    private Country country;

    @NotBlank(message = "latitude cannot be blank")
    @Column(name = "latitude")
    private String latitude;

    @NotBlank(message = "longtitude cannot be blank")
    @Column(name = "longtitude")
    private String longtitude;

    @NotBlank(message = "zipcode cannot be blank")
    @Column(name = "zipcode",  nullable = false)
    private String zipcode;


    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "home_images", joinColumns = @JoinColumn(name = "home_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "img_urls")
    private List<String> imgUrls;

    @Column(name = "open_booking", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openBooking;

    @Column(name = "close_booking", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeBooking;

    @Min(value = 1, message = "beds must be higher than 0")
    @Column(name = "beds",  nullable = false)
    private int beds;

    @Min(value = 1, message = "bedrooms must be higher than 0")
    @Column(name = "bedrooms",  nullable = false)
    private int bedrooms;
    @Min(value = 1, message = "capacity must be higher than 0")
    @Column(name = "capacity",  nullable = false)
    private int capacity;
    
    
    @Column(name = "rating")
    private Double rating;

    @ColumnDefault("0")
    @Column(name = "review_nums")
    private int reviewNums;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "home_category_id",
        referencedColumnName = "id"
    )
    private HomeCategory homeCategory;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "owner_id",
        referencedColumnName = "id"
    )
    private Host owner;

    
    @OneToOne(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Discount discount;
    
    @JsonIgnore
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings =  new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bookdate> bookdates =  new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlists = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HomeReview> homeReviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TenantReview> tenantReviews = new ArrayList<>();


    public Home( String title, Double price, String address, City city, Country country, String latitude, String longtitude, String zipcode, List<String> imgUrls, LocalDate openBooking, LocalDate closeBooking, int beds, int bedrooms, int capacity, HomeCategory homeCategory,  Host owner) {
        this.title = title;
        this.price = price;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.zipcode = zipcode;
        this.imgUrls = imgUrls;
        this.openBooking = openBooking;
        this.closeBooking = closeBooking;
        this.beds = beds;
        this.bedrooms = bedrooms;
        this.capacity = capacity;
        this.homeCategory = homeCategory;
        this.owner = owner;
    }

    
}
