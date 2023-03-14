package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Bookdate")
@Table(name = "bookdate", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "booking_id", "home_id"})})
@Getter
@Setter
public class Bookdate {
     @Id
    @SequenceGenerator(
        name = "bookdate_sequence",
        allocationSize = 1,
        sequenceName = "bookdate_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "bookdate_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    
    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    public Bookdate(LocalDate date, Booking booking, Home home) {
        this.date = date;
        this.booking = booking;
        this.home = home;
    }

    

}
