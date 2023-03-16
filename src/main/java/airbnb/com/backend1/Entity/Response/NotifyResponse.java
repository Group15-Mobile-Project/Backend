package airbnb.com.backend1.Entity.Response;

import java.time.LocalDate;

import airbnb.com.backend1.Entity.Enums.NotifyStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyResponse {
    private Long id;
    private NotifyStatus status;
    private boolean isRead;
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;
    @Column(name = "date_updated", nullable = false)
    private LocalDate dateUpdated;
    private UserResponse tenant;
    private HostResponse host;
    private Long homeId;
    private Long bookingId;
    private String bookingCode;
    
    public NotifyResponse(Long id, NotifyStatus status, boolean isRead, LocalDate dateCreated, LocalDate dateUpdated,
            UserResponse tenant, HostResponse host, Long homeId) {
        this.id = id;
        this.status = status;
        this.isRead = isRead;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.tenant = tenant;
        this.host = host;
        this.homeId = homeId;
    }

    

}
