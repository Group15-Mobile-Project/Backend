package airbnb.com.backend1.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import airbnb.com.backend1.Entity.Enums.NotifyStatus;
import airbnb.com.backend1.Entity.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Notify")
@Table(name = "notify")
@Getter
@Setter
public class Notify {
    @Id
    @SequenceGenerator(
        name = "notify_sequence",
        allocationSize = 1,
        sequenceName = "notify_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "notify_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotifyStatus status;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_updated", nullable = false)
    private LocalDate dateUpdated;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Users tenant;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private Host host;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "home_id", referencedColumnName = "id")
    private Home home;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    public Notify(NotifyStatus status, Users tenant, Host host, Home home, Booking booking, boolean isRead) {
        this.status = status;
        this.tenant = tenant;
        this.host = host;
        this.home = home;
        this.booking = booking;
        this.isRead = isRead;
    }

    public Notify(NotifyStatus status, Users tenant, Host host, Home home,  boolean isRead) {
        this.status = status;
        this.tenant = tenant;
        this.host = host;
        this.home = home;
        this.isRead = isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public boolean getIsRead() {
        return this.isRead;
    }

    
}
