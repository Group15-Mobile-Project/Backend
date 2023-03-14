package airbnb.com.backend1.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HostResponse {
    private Long id;
    private UserResponse user;
}
