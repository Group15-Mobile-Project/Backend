package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.TenantReview;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Entity.Response.TenantReviewResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class TenantReviewMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    HomeMapper homeMapper;
    @Autowired
    HostMapper hostMapper;
    @Autowired
    BookingMapper bookingMapper;

    public TenantReviewResponse mapTenantReviewToRespose(TenantReview review) {
        UserResponse tenant = userMapper.mapUserToResponse(review.getTenant());
        HomeResponse home = homeMapper.mapHomeToResponse(review.getHome());
        HostResponse host = hostMapper.mapHostToResponse(review.getHost());
        
        TenantReviewResponse res = new TenantReviewResponse(review.getId(), review.getContent(), review.getRating(), tenant, host, home, review.getBooking().getId(), review.getCreateDate(), review.getUpdateDate());

        return res;
    }
}
