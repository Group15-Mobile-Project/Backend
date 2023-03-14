package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Home_review")
@Table(name = "home_review", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "home_id"})})
@Getter
@Setter
public class HomeReview {
    @Id
    @SequenceGenerator(
        name = "home_review_sequence",
        allocationSize = 1,
        sequenceName = "home_review_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "home_review_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    
    @NotBlank(message = "content of review cannot be blank")
    @Column(name = "content", nullable = false)
    private String content;

    @Max(value = 5, message = "max rating is 5")
    @Min(value = 1, message = "min rating is 1")
    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    public HomeReview( String content, int rating,   Users user, Home home, Booking booking) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.home = home;
        this.booking = booking;
    }

    
}
