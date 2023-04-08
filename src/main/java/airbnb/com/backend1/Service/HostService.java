package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Entity.Response.HostStatistics;

public interface HostService {
    HostResponse getHostById(Long id);
    List<HostResponse> getAllHosts();
    List<HostResponse> getAllHostsByName(String name);
    HostResponse getHostByAuthUser();
    HostStatistics getHostStatistics();
}
