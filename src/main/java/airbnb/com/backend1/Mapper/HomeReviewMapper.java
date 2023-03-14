package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.HomeReview;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Entity.Response.HomeReviewResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class HomeReviewMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    HomeMapper homeMapper;

    public HomeReviewResponse mapHomeReviewToRes(HomeReview review) {
        UserResponse user = userMapper.mapUserToResponse(review.getUser());
        HomeResponse home = homeMapper.mapHomeToResponse(review.getHome());

        HomeReviewResponse res = new HomeReviewResponse(review.getId(), review.getContent(), review.getRating(), home, review.getBooking().getId(), user, review.getCreateDate(), review.getUpdateDate());
        
        return res;
    }
}
