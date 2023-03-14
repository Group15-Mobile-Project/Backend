package airbnb.com.backend1.Entity.Request;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TenantReviewRequest {

    private String content;
    private int rating;
    private Long tenantId;
    private Long homeId;
    private Long bookingId;
}
