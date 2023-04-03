package airbnb.com.backend1.Service.implementation;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.HomeReview;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Notify;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Enums.NotifyStatus;
import airbnb.com.backend1.Entity.Request.HomeReviewRequest;
import airbnb.com.backend1.Entity.Response.HomeReviewResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.HomeReviewMapper;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HomeReviewRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.NotifyRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.HomeReviewService;

@Service
public class HomeReviewServiceIml implements HomeReviewService {
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    HomeReviewRepos homeReviewRepos;
    @Autowired
    HomeReviewMapper mapper;
    @Autowired
    NotifyRepos notifyRepos;
    @Autowired
    HostRepos hostRepos;

    @Override
    public HomeReviewResponse getReviewById(Long id) {
        Optional<HomeReview> entity = homeReviewRepos.findById(id);
        if(entity.isPresent()) {
            HomeReview review = entity.get();
            return mapper.mapHomeReviewToRes(review);
        }
        throw new EntityNotFoundException("the review not found");
    }
    @Override
    public List<HomeReviewResponse> getReviewsByHome(Long homeId) {
        Home home = getHome(homeId);
        List<HomeReview> reviews = homeReviewRepos.findByHome(home);
        List<HomeReviewResponse> res = reviews.stream().map(review -> mapper.mapHomeReviewToRes(review)).collect(Collectors.toList());
        return res;

    }
    @Override
    public List<HomeReviewResponse> getReviewsByHomeAndSearching(Long homeId, String query) {
        if(query.isBlank()) {
            throw new BadResultException("the query search must not be blank");
        }
        Home home = getHome(homeId);
        List<HomeReview> reviews = homeReviewRepos.findByHome(home);
        List<HomeReviewResponse> res = reviews.stream().filter(review -> review.getContent().contains(query.toLowerCase())).map(review -> mapper.mapHomeReviewToRes(review)).collect(Collectors.toList());
        return res;
    }

    @Override
    public HomeReviewResponse getReviewByHomeAndUser(Long homeId, Long userId) {
        Optional<Users> entityUser = userRepos.findById(userId);
        Users user = isCheck(entityUser);
        Home home = getHome(homeId);
        if(user.getId() == home.getOwner().getUser().getId()) {
            throw new BadResultException("the owner of the home cannot add home review");
        }

        Optional<HomeReview> entity = homeReviewRepos.findByHomeAndUser(home, user);
        if(!entity.isPresent()) {
            throw new EntityExistingException("just allowed to add one review per home");
        }

        HomeReview review = entity.get();
        return mapper.mapHomeReviewToRes(review);
    }

    @Override
    public List<HomeReviewResponse> getReviewsByHost(Long hostId) {
        Optional<Host> entity = hostRepos.findById(hostId);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the host not found");
        }
        Host host = entity.get();
        List<HomeReview> reviews = homeReviewRepos.findByHost(hostId);
        List<HomeReviewResponse> res = reviews.stream().map(review -> mapper.mapHomeReviewToRes(review)).collect(Collectors.toList());
        return res;
    }

    @Override
    public HomeReviewResponse saveReview(HomeReviewRequest req) {
        Users authUser = getAuthUser();
        if(req.getContent().isBlank()) {
            throw new BadResultException("the content must not be blank");
        }
        if(authUser.getId() != req.getUserId()) {
            throw new BadResultException("unAuthorized to add review because of not an authenticated user");
        }
        Booking booking = getBooking(req.getBookingId());
        if(authUser.getId() != booking.getTenant().getId()) {
            throw new BadResultException("unAuthorized to add review because of not having any booking with the home");
           
        }
        Home home = getHome(req.getHomeId());
        if(authUser.getId() == home.getOwner().getUser().getId()) {
            throw new BadResultException("the owner of the home cannot add home review");
        }
        if(home.getId() != booking.getHome().getId()) {
            throw new BadResultException("the home is not the one in the booking");
        }

        Optional<HomeReview> entity = homeReviewRepos.findByHomeAndUser(home, authUser);
        if(entity.isPresent()) {
            throw new EntityExistingException("just allowed to add one review per home");
        }
        HomeReview review = new HomeReview(req.getContent().toLowerCase(), req.getRating(), authUser, home, booking);
        homeReviewRepos.save(review);

        authUser.getHomeReviews().add(review);
        booking.setHomeReview(review);
        // userRepos.save(authUser);
        bookingRepos.save(booking);
        
        Double avgRating;

        if(home.getRating() == null) {
            avgRating = Double.valueOf(String.valueOf(req.getRating()));
        } else {
            avgRating = (home.getRating() + req.getRating()) / 2;
        }
        home.setRating(avgRating);
        home.setReviewNums(home.getReviewNums() + 1);
        home.getHomeReviews().add(review);
        // homeRepos.save(home);

        Host host = home.getOwner();
        // Notify notify = new Notify(NotifyStatus.HOME_REVIEW, authUser, host, home, booking, false);
        Notify notify = new Notify(NotifyStatus.HOME_REVIEW, authUser, host, home, false);
        notifyRepos.save(notify);
        
        authUser.getNotifies().add(notify);
        host.getNotifies().add(notify);
        home.getNotifies().add(notify);

        userRepos.save(authUser);
        homeRepos.save(home);
        hostRepos.save(host);

        return mapper.mapHomeReviewToRes(review);
    }
    @Override
    public HomeReviewResponse updateReview(Long id, int rating, String content) {
        Users authUser = getAuthUser();
        Optional<HomeReview> entity = homeReviewRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the home review not found");
        }
        HomeReview review = entity.get();

        if(authUser.getId() != review.getUser().getId()) {
            throw new BadResultException("unAuthorized to add review because of not an authenticated user");
        }

        Home home = review.getHome();
        Double previousHomeRating = home.getRating() * 2 / review.getRating();
          
        review.setRating(rating);
        if(content.isBlank()) {
            throw new BadResultException("the content must not be blank");
        }
        review.setContent(content);
        homeReviewRepos.save(review);

        Double newHomeRating = (previousHomeRating + rating) /2;
        if(home.getReviewNums() > 1) {
             home.setRating(newHomeRating);
        } else {
            home.setRating(Double.valueOf(String.valueOf(rating)));
        }
        homeRepos.save(home);

        return mapper.mapHomeReviewToRes(review);
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
