package airbnb.com.backend1.Entity.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeReviewRequest {
    private String content;
    private int rating;
    private Long homeId;
    private Long bookingId;
    private Long userId;
}
