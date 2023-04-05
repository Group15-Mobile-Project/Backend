package airbnb.com.backend1.Mapper;

import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Bookdate;
import airbnb.com.backend1.Entity.Response.BookdateResponse;

@Component
public class BookdateMapper {
    public BookdateResponse mapBookdateToResponse(Bookdate bookdate) {
        BookdateResponse res = new BookdateResponse(bookdate.getId(), bookdate.getHome().getId(), bookdate.getDate(), bookdate.getBooking().getId());
        return res;
    } 
}
