package airbnb.com.backend1.Mapper;

import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Discount;
import airbnb.com.backend1.Entity.Response.DiscountResponse;
@Component
public class DiscountMapper {
    public DiscountResponse mapDiscountToResponse(Discount discount) {
        DiscountResponse res = new DiscountResponse(discount.getId(), discount.getOpenDate(), discount.getCloseDate(), discount.getDiscountRate(), discount.getHome().getId());
        return res;
    }
}
