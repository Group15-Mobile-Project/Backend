package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Wishlist;
import airbnb.com.backend1.Entity.Response.WishlistResponse;

public interface WishlistService {
    List<WishlistResponse> getWishlistsByAuthUser();
    List<WishlistResponse> getWishlistsByHome(Long homeId);
    WishlistResponse save(Long homeId, Long userId);
    void delete(Long id);
}
