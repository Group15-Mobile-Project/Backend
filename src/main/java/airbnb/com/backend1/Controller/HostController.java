package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Service.HostService;

@RestController
@RequestMapping("/api/hosts")
public class HostController {
    @Autowired
    HostService hostService;

    @GetMapping("/host/{id}")
    public ResponseEntity<HostResponse> getHostById(@PathVariable Long id) {
        return new ResponseEntity<HostResponse>(hostService.getHostById(id), HttpStatus.OK);
    }
    @GetMapping("/authUser")
    public ResponseEntity<HostResponse> getHostByAuthuser() {
        return new ResponseEntity<HostResponse>(hostService.getHostByAuthUser(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<HostResponse>> getAll() {
        return new ResponseEntity<List<HostResponse>>(hostService.getAllHosts(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hostName/{hostName}")
    public ResponseEntity<List<HostResponse>> getAllByHostName(@PathVariable String hostName) {
        return new ResponseEntity<List<HostResponse>>(hostService.getAllHostsByName(hostName), HttpStatus.OK);
    }
}
