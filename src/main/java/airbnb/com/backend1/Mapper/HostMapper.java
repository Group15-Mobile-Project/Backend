package airbnb.com.backend1.Mapper;

import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class HostMapper {
    public HostResponse mapHostToResponse(Host host) {
        Users user = host.getUser();
        UserResponse res = new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles(), user.isHost(), user.getImageurl(), user.getReviewNums());
        if(user.getHost() != null) {
            res.setHostId(user.getHost().getId());
        }
        return new HostResponse(host.getId(), res);
    }
}
