package airbnb.com.backend1.Mapper;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class UserMapper {
    

    public UserResponse mapUserToResponse(Users user) {
        UserResponse res = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles(), user.isHost(), user.getImageurl(), user.getReviewNums());
        if(user.getHost() != null) {
            res.setHostId(user.getHost().getId());
        }
        if(user.getRating() != null) {
            res.setRating(user.getRating());
        }
        return res;
    }

}
