package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Response.HomeResponse;
import airbnb.com.backend1.Entity.Response.HostResponse;

@Component
public class HomeMapper {
    @Autowired
    HostMapper hostmapMapper;
    @Autowired
    DiscountMapper discountMapper;
    public HomeResponse mapHomeToResponse(Home home) {
        HostResponse owner = hostmapMapper.mapHostToResponse(home.getOwner());

        HomeResponse res = new HomeResponse(home.getId(), home.getOpenBooking(), home.getCloseBooking(), home.getTitle(), home.getPrice(), home.getAddress(), home.getCity(), home.getCountry(), home.getLatitude(), home.getLongtitude(), home.getZipcode(), home.getImgUrls(), home.getBeds(), home.getBedrooms(), home.getCapacity(), home.getReviewNums(), home.getHomeCategory(), owner);
        if(home.getRating() != null) {
            res.setRating(home.getRating());
        }
        if(home.getDiscount() != null) {
            res.setDiscount(discountMapper.mapDiscountToResponse(home.getDiscount()));
        }
        return res;
    }
}
