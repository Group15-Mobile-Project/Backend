package airbnb.com.backend1.Controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.BookingRequest;
import airbnb.com.backend1.Entity.Response.BookingResponse;
import airbnb.com.backend1.Entity.Response.CountDiscoutResponse;
import airbnb.com.backend1.Service.BookingService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @GetMapping("/oldBookings/tenant/{tenantId}")
    public ResponseEntity<List<BookingResponse>> getOldBookingsByTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getOldBookingsByTenant(tenantId), HttpStatus.OK);
    }
    @GetMapping("/upcomingBookings/tenant/{tenantId}")
    public ResponseEntity<List<BookingResponse>> getUpcomingBookingsByTenant(@PathVariable Long tenantId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getUpcomingBookingsByTenant(tenantId), HttpStatus.OK);
    }
    @GetMapping("/oldBookings/home/{homeId}")
    public ResponseEntity<List<BookingResponse>> getOldBookingsByHome(@PathVariable Long homeId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getOldBookingsByHome(homeId), HttpStatus.OK);
    }

    @GetMapping("/upcomingBookings/home/{homeId}")
    public ResponseEntity<List<BookingResponse>> getUpcomingByHome(@PathVariable Long homeId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getUpcomingBookingsByHome(homeId), HttpStatus.OK);
    }

    @GetMapping("/oldBookings/host/{hostId}")
    public ResponseEntity<List<BookingResponse>> getOldBookingsByHost(@PathVariable Long hostId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getOldBookingsByHost(hostId), HttpStatus.OK);
    }

    @GetMapping("/upcomingBookings/host/{hostId}")
    public ResponseEntity<List<BookingResponse>> getUpcomingBookingsByHost(@PathVariable Long hostId) {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getUpcomingBookingsByHost(hostId), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/oldBookings/admin")
    public ResponseEntity<List<BookingResponse>> getoldBookingsForAdmin() {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getoldBookings(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/upcomingBooking/admin")
    public ResponseEntity<List<BookingResponse>> getUpcomingBookingsForAdmin() {
        return new ResponseEntity<List<BookingResponse>>(bookingService.getUpcomingBookings(), HttpStatus.OK);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponse> getByID(@PathVariable Long bookingId) {
        return new ResponseEntity<BookingResponse>(bookingService.getById(bookingId), HttpStatus.OK);
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest request) {
        return new ResponseEntity<BookingResponse>(bookingService.createBooking(request), HttpStatus.CREATED);
    }

    @PutMapping("/acceptbooking/{bookingId}")
    public ResponseEntity<BookingResponse> acceptBooking(@PathVariable Long bookingId) {
        return new ResponseEntity<BookingResponse>(bookingService.acceptBooking(bookingId), HttpStatus.OK);
    }
    @PutMapping("/unacceptbooking/{bookingId}")
    public ResponseEntity<BookingResponse> unAcceptBooking(@PathVariable Long bookingId) {
        return new ResponseEntity<BookingResponse>(bookingService.unAcceptBooking(bookingId), HttpStatus.OK);
    }

    @PutMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long bookingId, @RequestParam LocalDate checkInDate, @RequestParam LocalDate checkOutDate, @RequestParam Long days, @RequestParam Double totalPrice) {
        return new ResponseEntity<BookingResponse>(bookingService.updateBooking(bookingId, checkInDate, checkOutDate, days, totalPrice), HttpStatus.CREATED);
    }

    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<HttpStatus> deleteByID(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<HttpStatus>( HttpStatus.OK);
    }
    @GetMapping("/home/{homeId}/checkin/{checkin}/checkout/{checkout}")
    public ResponseEntity<CountDiscoutResponse> getDiscountCount(@PathVariable Long homeId, @PathVariable LocalDate checkin, @PathVariable LocalDate checkout) {
        return new ResponseEntity<CountDiscoutResponse>(bookingService.getCountDiscount(homeId, checkin, checkout), HttpStatus.OK);
    }

}
