package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Response.BookdateResponse;
import airbnb.com.backend1.Service.BookdateService;

@CrossOrigin
@RestController
@RequestMapping("/api/bookdates")
public class BookdateController {
    @Autowired
    BookdateService bookdateService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookdateResponse>> getByBooking(@PathVariable Long bookingId) {
        return new ResponseEntity<List<BookdateResponse>>(bookdateService.getAllByBooking(bookingId), HttpStatus.OK);
    }

    @GetMapping("/home/{homeId}")
    public ResponseEntity<List<BookdateResponse>> getByHome(@PathVariable Long homeId) {
        return new ResponseEntity<List<BookdateResponse>>(bookdateService.getAllByHome(homeId), HttpStatus.OK);
    }

    @GetMapping("/homeOfUpcomingTime/{homeId}")
    public ResponseEntity<List<BookdateResponse>> getByHomeOfUpcomingTime(@PathVariable Long homeId) {
        return new ResponseEntity<List<BookdateResponse>>(bookdateService.getAllByHomeFollowCurrentTime(homeId), HttpStatus.OK);
    }
    @GetMapping("/authUser/upcoming")
    public ResponseEntity<List<BookdateResponse>> getByAuthUserOfUpcomingTime() {
        return new ResponseEntity<List<BookdateResponse>>(bookdateService.getAllByAuthUserFollowCurrentTime(), HttpStatus.OK);
    }
}
