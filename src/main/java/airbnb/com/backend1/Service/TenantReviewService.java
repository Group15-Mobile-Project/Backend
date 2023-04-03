package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Request.TenantReviewRequest;
import airbnb.com.backend1.Entity.Response.TenantReviewResponse;

public interface TenantReviewService {
    //this method is used to get all tenant reviews of a user including tenant or authuser based on its userId.
    List<TenantReviewResponse> getReviewsByTenant(Long tenantId);
    TenantReviewResponse getByTenantAndHost(Long tenantId, Long hostId);

    TenantReviewResponse getById(Long id);
    TenantReviewResponse save(TenantReviewRequest req);
    TenantReviewResponse update(Long id, int rating, String content);
}
