package airbnb.com.backend1.Entity;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Enums.Role;
import jakarta.persistence.*;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Wishlist")
@Table(name = "wishlist", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "home_id"})})
@Getter
@Setter
public class Wishlist {
    @Id
    @SequenceGenerator(
        name = "wishlist_sequence",
        allocationSize = 1,
        sequenceName = "wishlist_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "wishlist_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    public Wishlist(Users user, Home home) {
        this.user = user;
        this.home = home;
    }

    
}
