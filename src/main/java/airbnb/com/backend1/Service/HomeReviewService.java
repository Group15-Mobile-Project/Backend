package airbnb.com.backend1.Service;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.HomeReview;
import airbnb.com.backend1.Entity.Request.HomeReviewRequest;
import airbnb.com.backend1.Entity.Response.HomeReviewResponse;


public interface HomeReviewService {
    HomeReviewResponse saveReview(HomeReviewRequest req);
    HomeReviewResponse updateReview(Long id, int rating, String content);
    HomeReviewResponse getReviewById(Long id);
    HomeReviewResponse getReviewByHomeAndUser(Long homeId, Long userId);
    List<HomeReviewResponse> getReviewsByHome(Long homeId);
    List<HomeReviewResponse> getReviewsByHomeAndSearching(Long homeId, String query);
    List<HomeReviewResponse> getReviewsByHost(Long hostId);
}
