package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Response.WishlistResponse;
import airbnb.com.backend1.Service.WishlistService;

@CrossOrigin
@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {
    @Autowired
    WishlistService wishlistService;

    @GetMapping("/authUser")
    public ResponseEntity<List<WishlistResponse>> getByAuthUsers() {
        return new ResponseEntity<List<WishlistResponse>>(wishlistService.getWishlistsByAuthUser(), HttpStatus.OK);
    }
    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<WishlistResponse>> getByHome(@PathVariable Long homeId) {
        return new ResponseEntity<List<WishlistResponse>>(wishlistService.getWishlistsByHome(homeId), HttpStatus.OK);
    }
    @PostMapping("/home/{homeId}/user/{userId}")
    public ResponseEntity<WishlistResponse> save(@PathVariable Long homeId, @PathVariable Long userId) {
        return new ResponseEntity<WishlistResponse>(wishlistService.save(homeId, userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<HttpStatus> save(@PathVariable Long id) {
        wishlistService.delete(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
