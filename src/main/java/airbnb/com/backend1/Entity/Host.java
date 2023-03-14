package airbnb.com.backend1.Entity;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Users;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Host")
@Table(name = "host")
@Getter
@Setter
public class Host {
    @Id
    @SequenceGenerator(
        name = "host_sequence",
        allocationSize = 1,
        sequenceName = "host_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "host_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Home> homes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TenantReview> tenantReviews = new ArrayList<>();

    public Host(Users user) {
        this.user = user;
    }

    
}
