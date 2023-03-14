package airbnb.com.backend1.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Enums.Role;
import jakarta.persistence.*;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Users")
@Table(name = "users")
@Getter
@Setter
public class Users {
    @Id
    @SequenceGenerator(
        name = "users_sequence",
        allocationSize = 1,
        sequenceName = "users_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "users_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "has_host", nullable = false)
    private boolean isHost;
    @Column(name = "imageurl")
    private String imageurl;

   
    @Column(name = "rating")
    private Double rating;

    @ColumnDefault("0")
    @Column(name = "review_nums")
    private int reviewNums;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Host host;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlists = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<HomeReview> homeReviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<TenantReview> tenantReviews = new ArrayList<>();

    public Users(String username, String email, String password, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.isHost = false;
    }
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isHost = false;
    }

    public Users(Long id, String username, String email, String password, boolean isHost, String imageurl,
            List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isHost = isHost;
        this.imageurl = imageurl;
        this.roles = roles;
    }

    public void setIsHost(boolean isHost) {
        this.isHost = isHost;
    }
    
}
