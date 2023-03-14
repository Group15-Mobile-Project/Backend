package airbnb.com.backend1.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {
    private Long id;
    private UserResponse user;
    private HomeResponse homeResponse;
}
