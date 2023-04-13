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

import airbnb.com.backend1.Entity.Request.TenantReviewRequest;
import airbnb.com.backend1.Entity.Response.TenantReviewResponse;
import airbnb.com.backend1.Service.TenantReviewService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/tenantReviews")
public class TenantReviewController {
    @Autowired
    TenantReviewService reviewService;

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<TenantReviewResponse>> getAllByTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<List<TenantReviewResponse>>(reviewService.getReviewsByTenant(tenantId), HttpStatus.OK);
    }
    @GetMapping("/tenant/{tenantId}/host/{hostId}")
    public ResponseEntity<TenantReviewResponse> getByTenantAndAuthHost(@PathVariable Long tenantId, @PathVariable Long hostId) {
        return new ResponseEntity<TenantReviewResponse>(reviewService.getByTenantAndHost(tenantId, hostId), HttpStatus.OK);
    }
    @GetMapping("/review/{id}")
    public ResponseEntity<TenantReviewResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<TenantReviewResponse>(reviewService.getById(id), HttpStatus.OK);
    }
    @PostMapping("/review")
    public ResponseEntity<TenantReviewResponse> addReview(@Valid @RequestBody TenantReviewRequest req) {
        return new ResponseEntity<TenantReviewResponse>(reviewService.save(req), HttpStatus.OK);
    }
    @PutMapping("/review/{id}")
    public ResponseEntity<TenantReviewResponse> updateReview(@PathVariable Long id, @RequestParam int rating, @RequestParam String content) {
        return new ResponseEntity<TenantReviewResponse>(reviewService.update(id, rating, content), HttpStatus.OK);
    }
}
