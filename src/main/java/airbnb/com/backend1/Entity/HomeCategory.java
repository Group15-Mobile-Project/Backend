package airbnb.com.backend1.Entity;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.sqm.CastType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "HomeCategory")
@Table(name = "homeCategory")
@Getter
@Setter
public class HomeCategory {
    @Id
    @SequenceGenerator(
        name = "homeCategory_sequence",
        allocationSize = 1,
        sequenceName = "homeCategory_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "homeCategory_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @NotBlank(message = "name cannot be blank")
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @NotBlank(message = "imageUrl cannot be blank")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "homeCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Home> homes = new ArrayList<>();

    public HomeCategory( String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    
}
