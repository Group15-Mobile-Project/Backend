package airbnb.com.backend1.Service;

import java.time.LocalDate;
import java.util.List;

import airbnb.com.backend1.Entity.Request.BookingRequest;
import airbnb.com.backend1.Entity.Response.BookingResponse;
import airbnb.com.backend1.Entity.Response.CountDiscoutResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse acceptBooking(Long bookingId);
    BookingResponse unAcceptBooking(Long bookingId);

    BookingResponse updateBooking(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, Long days, Double totalPrice);
   
    BookingResponse getById(Long BookingId);
    List<BookingResponse> getOldBookingsByTenant(Long tenantId);
    List<BookingResponse> getUpcomingBookingsByTenant(Long tenantId);
    List<BookingResponse> getOldBookingsByHome(Long homeId);
    List<BookingResponse> getUpcomingBookingsByHome(Long homeId);
    List<BookingResponse> getUpcomingBookingsByHost(Long hostId);
    List<BookingResponse> getOldBookingsByHost(Long hostId);
    // for admin
    List<BookingResponse> getoldBookings();
      // for admin
    List<BookingResponse> getUpcomingBookings();
  
    void deleteBooking(Long bookingId);
    CountDiscoutResponse getCountDiscount(Long homeId, LocalDate checkin, LocalDate checkout);
}
