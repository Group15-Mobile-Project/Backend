package airbnb.com.backend1.Service;

import java.time.LocalDate;
import java.util.List;

import airbnb.com.backend1.Entity.Response.BookdateResponse;

public interface BookdateService {
    List<BookdateResponse> getAllByBooking(Long bookingId);
    List<BookdateResponse> getAllByHome(Long homeId);
    List<BookdateResponse> getAllByHomeFollowCurrentTime(Long homeId);
    List<BookdateResponse> getAllByAuthUserFollowCurrentTime();
}
