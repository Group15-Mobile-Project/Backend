package airbnb.com.backend1.Service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Bookdate;
import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.BookdateResponse;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.BookdateMapper;
import airbnb.com.backend1.Repository.BookdateRepos;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.BookdateService;

@Service
public class BookdateServiceIml implements BookdateService {
    @Autowired
    BookdateRepos bookdateRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostRepos hostRepos;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    BookdateMapper bookdateMapper;

    @Override
    public List<BookdateResponse> getAllByBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        List<Bookdate> dates = bookdateRepos.findByBooking(booking);
        List<BookdateResponse> res = dates.stream().map(dat ->      bookdateMapper.mapBookdateToResponse(dat)).collect(Collectors.toList());
        return res;
    }

    @Override
    public List<BookdateResponse> getAllByHome(Long homeId) {
        Home home = getHome(homeId);
        List<Bookdate> dates = bookdateRepos.findByHome(home);
        List<BookdateResponse> res = dates.stream().map(dat ->      bookdateMapper.mapBookdateToResponse(dat)).collect(Collectors.toList());
        return res;
    }

    @Override
    public List<BookdateResponse> getAllByHomeFollowCurrentTime(Long homeId) {
        LocalDate currentTime = LocalDate.now();
        Home home = getHome(homeId);
        List<Bookdate> dates = bookdateRepos.findByHome(home);
        List<BookdateResponse> res = dates.stream().filter(dat -> dat.getDate().isAfter(currentTime)).map(dat ->  bookdateMapper.mapBookdateToResponse(dat)).collect(Collectors.toList());
        return res;
    }
    @Override
    public List<BookdateResponse> getAllByAuthUserFollowCurrentTime() {
        Users authUser = getAuthUser();
        Host host = authUser.getHost();
        List<Bookdate> dates = bookdateRepos.findByHostId(host.getId());
        LocalDate currentTime = LocalDate.now();
        List<BookdateResponse> res = dates.stream().filter(dat -> dat.getDate().isAfter(currentTime)).map(dat ->      bookdateMapper.mapBookdateToResponse(dat)).collect(Collectors.toList());
        return res;
    }

    private Users isCheck(Optional<Users> entity) {
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the user not found");
    }
    private Users getAuthUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        return user;
    }
    private Home getHome(Long homeId) {
        Optional<Home> entity = homeRepos.findById(homeId);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the home not found");
    }

    private Booking getBooking(Long bookingId) {
        Optional<Booking> entity = bookingRepos.findById(bookingId);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the booking not found");
    }
    
}
