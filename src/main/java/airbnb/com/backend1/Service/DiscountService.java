package airbnb.com.backend1.Service;

import java.time.LocalDate;

import airbnb.com.backend1.Entity.Request.DiscountRequest;
import airbnb.com.backend1.Entity.Response.DiscountResponse;

public interface DiscountService {
    DiscountResponse save(DiscountRequest request);
    DiscountResponse update(Long id, DiscountRequest discountRequest);
}
