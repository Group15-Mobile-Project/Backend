package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.TenantReview;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.TenantReviewRequest;
import airbnb.com.backend1.Entity.Response.TenantReviewResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.TenantReviewMapper;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.TenantReviewRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.TenantReviewService;

@Service
public class TenantReviewServiceIml implements TenantReviewService {
    @Autowired
    TenantReviewRepos reviewRepos;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostRepos hostRepos;
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    TenantReviewMapper mapper;
    @Override
    public TenantReviewResponse getById(Long id) {
       Optional<TenantReview> entity = reviewRepos.findById(id);
       if(!entity.isPresent()) {
        throw new EntityNotFoundException("the tenant review not found");
       }
       TenantReview review = entity.get();
       return mapper.mapTenantReviewToRespose(review);
    }
    @Override
    public TenantReviewResponse getByTenantAndAuthHost(Long tenantId) {
        Users tenant = getTenant(tenantId);
        Users authUser = getAuthUser();
        Host host = authUser.getHost();
        Optional<TenantReview> entity = reviewRepos.findByTenantAndHost(tenant, host);
        if(!entity.isPresent()) {
         throw new EntityNotFoundException("the tenant review not found");
        }
        TenantReview review = entity.get();
        return mapper.mapTenantReviewToRespose(review);

    }
    @Override
    public List<TenantReviewResponse> getReviewsByTenant(Long tenantId) {
        Users tenant = getTenant(tenantId);
        List<TenantReview> reviews = reviewRepos.findByTenant(tenant);
        List<TenantReviewResponse> res = reviews.stream().map(review -> mapper.mapTenantReviewToRespose(review)).collect(Collectors.toList());
        return res;
    }
    @Override
    public TenantReviewResponse save(TenantReviewRequest req) {
        Users authUser = getAuthUser();
        Host host = authUser.getHost();
        Users tenant = getTenant(req.getTenantId());
        Booking booking = getBooking(req.getBookingId());
        Home home = getHome(req.getHomeId());

        if(req.getContent().isBlank()) {
            throw new BadResultException("the content must not be blank");
        }

        if(host.getId() != home.getOwner().getId()) {
            throw new BadResultException("unAuthorized to add tenant review because of not being a host of the home");
        }
        if(home.getId() != booking.getHome().getId()) {
            throw new BadResultException("the home is not the one in the booking");
        }
        
        if(tenant.getId() != booking.getTenant().getId()) {
            throw new BadResultException("the tenat is not the one in the booking");
        }

        Optional<TenantReview> entity = reviewRepos.findByTenantAndHost(tenant, host);
        if(entity.isPresent()) {
            throw new EntityExistingException("the tenant review exist");
        }
        TenantReview review = new TenantReview(req.getContent().toLowerCase(), req.getRating(), tenant, host, home, booking);
        reviewRepos.save(review);

        Double tenantRating;
        if(tenant.getRating() != null) {
            tenantRating = (tenant.getRating()  + req.getRating()) / 2;
        } else {
            tenantRating = Double.valueOf(String.valueOf(req.getRating()));
        }
        tenant.setRating(tenantRating);
        tenant.getTenantReviews().add(review);
        tenant.setReviewNums(tenant.getReviewNums() + 1);

        host.getTenantReviews().add(review);
        home.getTenantReviews().add(review);
        booking.setTenantReview(review);

        homeRepos.save(home);
        hostRepos.save(host);
        bookingRepos.save(booking);
        userRepos.save(tenant);

        return mapper.mapTenantReviewToRespose(review);

    }
    @Override
    public TenantReviewResponse update(Long id, int rating, String content) {
        Optional<TenantReview> entity = reviewRepos.findById(id);
        if(!entity.isPresent()) {
         throw new EntityNotFoundException("the tenant review not found");
        }
        TenantReview review = entity.get();
        Users authUser = getAuthUser();
        Host host = authUser.getHost();
        if(host.getId() != review.getHost().getId()) {
            throw new BadResultException("unAuthorized to update tenant review");
        }
        Users tenant = review.getTenant();
        Double previousTenantRating = tenant.getRating() * 2 / review.getRating();

        if(content.isBlank()) {
            throw new BadResultException("the content must not be blank");
        }
        review.setContent(content);
        review.setRating(rating);
        reviewRepos.save(review);

        Double newTenantRating = (previousTenantRating + rating) /2;
        if(tenant.getReviewNums()  > 1) {
            tenant.setRating(newTenantRating);
       } else {
           tenant.setRating(Double.valueOf(String.valueOf(rating)));
       }

       userRepos.save(tenant);
       return mapper.mapTenantReviewToRespose(review);
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

    private Users getTenant(Long tenantId) {
        
        Optional<Users> entity = userRepos.findById(tenantId);
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

    private Host getHostById(Long id) {
        Optional<Host> entity = hostRepos.findById(id);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the host not found");
    }
}
