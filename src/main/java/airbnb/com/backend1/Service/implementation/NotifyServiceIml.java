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
import airbnb.com.backend1.Entity.Notify;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Enums.NotifyStatus;
import airbnb.com.backend1.Entity.Response.NotifyResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.NotifyMapper;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.NotifyRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.NotifyService;

@Service
public class NotifyServiceIml implements NotifyService {
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostRepos hostRepos;
    @Autowired
    NotifyRepos notifyRepos;
    @Autowired
    NotifyMapper mapper;
    @Override
    public List<NotifyResponse> getAllByAuthHost() {
        Users authUser = getAuthUser();
        if(authUser == null) {
            throw new BadResultException("unauthorized to get notification");
        }
        Host host = authUser.getHost();
        List<Notify> notifies = notifyRepos.findByHost(host).stream().filter(notify -> (notify.getStatus().equals(NotifyStatus.PENDING_BOOKING) || notify.getStatus().equals(NotifyStatus.HOME_REVIEW) || notify.getStatus().equals(NotifyStatus.UPDATE_BOOKING) || notify.getStatus().equals(NotifyStatus.CANCEL_BOOKING)) && notify.getIsRead() == false).collect(Collectors.toList());
        notifies.sort((a, b) -> b.getDateUpdated().compareTo(a.getDateUpdated()));

        List<NotifyResponse> res = notifies.stream().map(notify -> mapper.mapNotifyToResponse(notify)).collect(Collectors.toList());

        return res;
    }
    @Override
    public List<NotifyResponse> getAllByAuthTenant() {
        Users authUser = getAuthUser();
        if(authUser == null) {
            throw new BadResultException("unauthorized to get notification");
        }
        List<Notify> notifies = notifyRepos.findByTenant(authUser).stream().filter(notify -> !notify.getStatus().equals(NotifyStatus.PENDING_BOOKING) && !notify.getStatus().equals(NotifyStatus.HOME_REVIEW) && !notify.getStatus().equals(NotifyStatus.UPDATE_BOOKING) &&  !notify.getStatus().equals(NotifyStatus.CANCEL_BOOKING) && notify.getIsRead() == false).collect(Collectors.toList());
        notifies.sort((a, b) -> b.getDateUpdated().compareTo(a.getDateUpdated()));

        List<NotifyResponse> res = notifies.stream().map(notify -> mapper.mapNotifyToResponse(notify)).collect(Collectors.toList());

        return res;
    }

    @Override
    public NotifyResponse updateFinishReading(Long id) {
        Optional<Notify> entity = notifyRepos.findById(id);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the notify not found");
        }
        Notify notify = entity.get();
        Users authUser = getAuthUser();
        if(authUser == null) {
            throw new BadResultException("unauthorized to update notification");
        }
        if(authUser.getId() == notify.getTenant().getId() || authUser.getId() == notify.getHost().getUser().getId()) {
            notify.setIsRead(true);
            notifyRepos.save(notify);
    
            return mapper.mapNotifyToResponse(notify);
        } else {
            throw new BadResultException("unauthorized to update the notification");
        } 
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
