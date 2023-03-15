package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Response.NotifyResponse;
import airbnb.com.backend1.Service.NotifyService;

@RestController
@RequestMapping("/api/notifies")
public class NotifyController {
    @Autowired
    NotifyService notifyService;

    @GetMapping("/authTenant")
    public ResponseEntity<List<NotifyResponse>> getByAuthTenant() {
        return new ResponseEntity<List<NotifyResponse>>(notifyService.getAllByAuthTenant(), HttpStatus.OK);
    }
    @GetMapping("/authHost")
    public ResponseEntity<List<NotifyResponse>> getByAuthHost() {
        return new ResponseEntity<List<NotifyResponse>>(notifyService.getAllByAuthHost(), HttpStatus.OK);
    }
    @PutMapping("/notify/{id}")
    public ResponseEntity<NotifyResponse> updateFinnishReading(@PathVariable Long id) {
        return new ResponseEntity<NotifyResponse>(notifyService.updateFinishReading(id), HttpStatus.OK);
    }
}
