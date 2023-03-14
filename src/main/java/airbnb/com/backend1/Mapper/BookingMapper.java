package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Response.BookingResponse;

@Component
public class BookingMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    HomeMapper homeMapper;
    public BookingResponse mapBookingToResponse(Booking booking) {
        BookingResponse res = new BookingResponse( booking.getId() ,booking.getBookingCode(), booking.getGuests(), booking.getDays(), booking.getTotalPrice(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getIsPaid(), homeMapper.mapHomeToResponse(booking.getHome()), userMapper.mapUserToResponse(booking.getTenant()), booking.getCreateDate(), booking.getUpdateDate(), booking.getStatus());
       
            res.setPriceAfterDiscount(booking.getPriceAfterDiscount());
        
        return res;
    }
}
