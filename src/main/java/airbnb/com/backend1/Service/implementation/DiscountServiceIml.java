package airbnb.com.backend1.Service.implementation;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Discount;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.DiscountRequest;
import airbnb.com.backend1.Entity.Response.DiscountResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.DiscountMapper;
import airbnb.com.backend1.Repository.DiscountRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.DiscountService;

@Service
public class DiscountServiceIml implements DiscountService {
    @Autowired
    DiscountRepos discountRepos;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    DiscountMapper discountMapper;

    @Override
    public DiscountResponse save(DiscountRequest request) {
        Users authUser = getAuthUser();
        Home home = getHome(request.getHomeId());
        Host host = home.getOwner();
        if(authUser.getId() != host.getUser().getId()) {
            throw new BadResultException("unAuthorized to create discount for the home");
        }
        boolean isValidDate = checkValidDateOfdiscount(request);
        if(isValidDate == false) {
            throw new BadResultException("the dates of discount are not valid");
        }

        boolean isValidateToHome = checkValidDateOfdiscountToHome(home, request) ;
        if(isValidateToHome == false) {
            throw new BadResultException("the dates of discount are not valid to the home");
        }

        Discount discount = new Discount(request.getOpenDate(), request.getCloseDate(), request.getDiscountRate(), home);
        discountRepos.save(discount);
        home.setDiscount(discount);
        homeRepos.save(home);
        return discountMapper.mapDiscountToResponse(discount);

    }

    @Override
    public DiscountResponse update(Long id, DiscountRequest req) {
        Optional<Discount> entity = discountRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the discount of home not found");
        }
        Discount discount = entity.get();
        Home home = discount.getHome();
        LocalDate currentTime = LocalDate.now();

        boolean isValidDate = checkValidDateOfdiscount(req);
        if(isValidDate == false) {
            throw new BadResultException("the dates of discount are not valid");
        }

        boolean isValidateToHome = checkValidDateOfdiscountToHome(home, req) ;
        if(isValidateToHome == false) {
            throw new BadResultException("the dates of discount are not valid to the home");
        }

        if(!req.getOpenDate().isEqual(discount.getOpenDate()) && req.getOpenDate().isAfter(currentTime)) {
            discount.setOpenDate(req.getOpenDate());
        }
        if(!req.getCloseDate().isEqual(discount.getCloseDate()) && req.getCloseDate().isAfter(currentTime)) {
            discount.setCloseDate(req.getCloseDate());
        }
        if(discount.getCloseDate().isEqual(discount.getOpenDate())) {
            throw new BadResultException("the close and open dates are not valid");
        }
        if(req.getDiscountRate() != discount.getDiscountRate() && req.getDiscountRate() <100 && req.getDiscountRate() > 0 ) {
            discount.setDiscountRate(req.getDiscountRate());
        }
        discountRepos.save(discount);
        return discountMapper.mapDiscountToResponse(discount);
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
    private boolean checkValidDateOfdiscount(DiscountRequest request) {
      
        LocalDate currentTime = LocalDate.now();
        if(request.getOpenDate().isEqual(currentTime) && request.getCloseDate().isAfter(currentTime)) {
            return true;
        }
        if(request.getOpenDate().isAfter(currentTime) && request.getCloseDate().isAfter(request.getOpenDate())) {
            return true;
        }
        return false;
    }
    private boolean checkValidDateOfdiscountToHome(Home home, DiscountRequest request) {
      
        LocalDate currentTime = LocalDate.now();
       if(request.getOpenDate().isBefore(home.getOpenBooking())) {
        return false;
       }
       if(request.getCloseDate().isAfter(home.getCloseBooking())) {
        return false;
       }
       return true;
    }
}
