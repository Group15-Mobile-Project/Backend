package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Validation.IsDiscount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Discount")
@Table(name = "discount")
@Getter
@Setter
@IsDiscount
public class Discount {
    @Id
    @SequenceGenerator(
        name = "discount_sequence",
        allocationSize = 1,
        sequenceName = "discount_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "discount_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "open_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate openDate;

    @Column(name = "close_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    @Min(value = 1, message = "discount rate must be higher than 0")
    @Max(value = 100, message = "discount rate must be smaller than 100")
    @Column(name = "discount_rate",  nullable = false)
    private int discountRate;

    @JsonIgnore
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    public Discount(LocalDate openDate, LocalDate closeDate, int discountRate,
            Home home) {
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.discountRate = discountRate;
        this.home = home;
    }

    @Override
    public String toString() {
        return "Discount [id=" + id + ", openDate=" + openDate + ", closeDate=" + closeDate + ", discountRate="
                + discountRate + ", home=" + home + "]";
    }

    
    

}
