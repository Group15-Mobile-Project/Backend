package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.HomeReview;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Response.HostResponse;
import airbnb.com.backend1.Entity.Response.HostStatistics;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.HostMapper;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HomeReviewRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.HostService;


@Service 
public class HostServiceiml implements HostService {
    @Autowired
    HostRepos hostRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostMapper hostMapper;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    HomeReviewRepos homeReviewRepos;

    @Override
    public List<HostResponse> getAllHosts() {
       List<Host> hosts = hostRepos.findAll();
        hosts.sort((a, b) -> a.getUser().getUsername().compareTo(b.getUser().getUsername()));
       List<HostResponse> res = hosts.stream().map(hos -> hostMapper.mapHostToResponse(hos)).collect(Collectors.toList());
       return res;
    }

    @Override
    public List<HostResponse> getAllHostsByName(String name) {
        List<Host> hosts = hostRepos.findByUsername(name);
        hosts.sort((a, b) -> a.getUser().getUsername().compareTo(b.getUser().getUsername()));
       List<HostResponse> res = hosts.stream().map(hos -> hostMapper.mapHostToResponse(hos)).collect(Collectors.toList());
       return res;
    }



    @Override
    public HostResponse getHostById(Long id) {
        Optional<Host> entity = hostRepos.findById(id);
        if(entity.isPresent()) {
            return hostMapper.mapHostToResponse(entity.get());
        }
        throw new EntityNotFoundException("the host not found");
    }

    @Override
    public HostResponse getHostByAuthUser() {
       Users authUser = getAuthUser();
       Optional<Host> entity = hostRepos.findByUserId(authUser.getId());
       if(entity.isPresent()) {
           return hostMapper.mapHostToResponse(entity.get());
       }
        throw new EntityNotFoundException("the host not found");
    }
    @Override
    public HostStatistics getHostStatistics() {
        Users authUser = getAuthUser();
        Host host = authUser.getHost();
        List<Home> homes = homeRepos.findByOwner(host);
        int homesNumber = homeRepos.findNumberHomesByHost(host.getId());
        List<Booking> bookings = bookingRepos.findByHost(host.getId());
        List<HomeReview> reviews = homeReviewRepos.findByHost(host.getId());
        double earnings = bookings.stream().reduce(0.0, (total, a) -> total + a.getPriceAfterDiscount  (), Double::sum);
        System.out.println("earnings " + earnings);
        System.out.println("number of homes " + homesNumber);

        HostResponse hostResponse = hostMapper.mapHostToResponse(host);
        HostStatistics statistic = new HostStatistics(hostResponse, earnings, bookings.size(), homesNumber, reviews.size());

        if(reviews.size() > 0) {
            // double overallRating = reviews.stream().reduce(0, (total, a) -> total + a.getRating(), Integer::sum) / reviews.size();
            int totalRating = reviews.stream().mapToInt(review -> review.getRating()).sum() ;
            System.out.println("rating " + totalRating);
            statistic.setRating(Math.round(totalRating / reviews.size()));
        }

        return statistic;
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
}
