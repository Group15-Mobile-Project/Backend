package airbnb.com.backend1.Entity.Response;

import java.util.List;

import airbnb.com.backend1.Entity.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private List<Role> roles;
    private boolean hasHost;
    private Long hostId;
    private String imgUrls;
    private Double rating;
    private int reviewNums;

    public UserResponse(Long id, String username, String email, List<Role> roles, boolean hasHost,String imgUrls, int reviewNums) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.hasHost = hasHost;
        this.imgUrls = imgUrls;
        this.reviewNums = reviewNums;
    }

    

    
}
