package airbnb.com.backend1.Entity;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Country")
@Table(name = "country")
@Getter
@Setter
public class Country {
    @Id
    @SequenceGenerator(
        name = "country_sequence",
        allocationSize = 1,
        sequenceName = "country_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "country_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    @NotBlank(message = "name must not be blank")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Home> homes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<City> cities = new ArrayList<>();

    public Country(String name) {
        this.name = name;
    }

    
}
