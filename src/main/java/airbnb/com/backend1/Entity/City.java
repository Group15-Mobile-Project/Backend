package airbnb.com.backend1.Entity;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "City")
@Table(name = "city")
@Getter
@Setter
public class City {
    @Id
    @SequenceGenerator(
        name = "city_sequence",
        allocationSize = 1,
        sequenceName = "city_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "city_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @JsonIgnore
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Home> homes = new ArrayList<>();

    public City(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    
}
