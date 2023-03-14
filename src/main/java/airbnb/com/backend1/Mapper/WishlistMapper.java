package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Wishlist;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;
import airbnb.com.backend1.Entity.Response.WishlistResponse;

@Component
public class WishlistMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired 
    HomeMapper homeMapper;

    public WishlistResponse mapWishlistToResponse(Wishlist wishlist) {
        UserResponse user = userMapper.mapUserToResponse(wishlist.getUser());
        HomeResponse home = homeMapper.mapHomeToResponse(wishlist.getHome());

        WishlistResponse res = new WishlistResponse(wishlist.getId(), user, home);
        return res;
    }
}
