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
@Entity(name = "Tenant_review")
@Table(name = "tenant_review", uniqueConstraints = {@UniqueConstraint(columnNames = {"tenant_id", "host_id"})})
@Getter
@Setter
public class TenantReview {
    @Id
    @SequenceGenerator(
        name = "tenant_review_sequence",
        allocationSize = 1,
        sequenceName = "tenant_review_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "tenant_review_sequence"
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

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updateDate;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Users tenant;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private Host host;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    public TenantReview( String content, int rating,  Users tenant, Host host, Home home, Booking booking) {
        this.content = content;
        this.rating = rating;
        this.tenant = tenant;
        this.host = host;
        this.home = home;
        this.booking = booking;
    }

    
}
