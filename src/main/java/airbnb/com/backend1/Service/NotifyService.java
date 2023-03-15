package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Response.NotifyResponse;

public interface NotifyService {
    List<NotifyResponse> getAllByAuthHost();
    List<NotifyResponse> getAllByAuthTenant();
    NotifyResponse updateFinishReading(Long id); 
}
