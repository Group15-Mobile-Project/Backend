package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.HomeReviewRequest;
import airbnb.com.backend1.Entity.Response.HomeReviewResponse;
import airbnb.com.backend1.Service.HomeReviewService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/homeReviews")
public class HomeReviewController {
    @Autowired
    HomeReviewService reviewService;

    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<HomeReviewResponse>> getByHome(@PathVariable Long homeId) {
        return new ResponseEntity<List<HomeReviewResponse>>(reviewService.getReviewsByHome(homeId), HttpStatus.OK);
    }
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<HomeReviewResponse>> getByhost(@PathVariable Long hostId) {
        return new ResponseEntity<List<HomeReviewResponse>>(reviewService.getReviewsByHost(hostId), HttpStatus.OK);
    }
    @GetMapping("/home/{homeId}/search")
    public ResponseEntity<List<HomeReviewResponse>> getByHomeAndQuerySearch(@PathVariable Long homeId, @RequestParam String query) {
        return new ResponseEntity<List<HomeReviewResponse>>(reviewService.getReviewsByHomeAndSearching(homeId, query), HttpStatus.OK);
    }
    @GetMapping("/review/{id}")
    public ResponseEntity<HomeReviewResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<HomeReviewResponse>(reviewService.getReviewById(id), HttpStatus.OK);
    }

    @GetMapping("/home/{homeId}/user/{userId}")
    public ResponseEntity<HomeReviewResponse> getByHomeAndAuthUser(@PathVariable Long homeId, @PathVariable Long userId) {
        return new ResponseEntity<HomeReviewResponse>(reviewService.getReviewByHomeAndUser(homeId, userId), HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity<HomeReviewResponse> addReview(@Valid @RequestBody HomeReviewRequest req) {
        return new ResponseEntity<HomeReviewResponse>(reviewService.saveReview(req), HttpStatus.CREATED);
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<HomeReviewResponse> updateReview(@PathVariable Long id,  @RequestParam int rating, @RequestParam String content) {
        return new ResponseEntity<HomeReviewResponse>(reviewService.updateReview(id, rating, content), HttpStatus.OK);
    }
}
