package airbnb.com.backend1.Service.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.stripe.model.giftcards.Card.CreatedBy.Checkout;

import airbnb.com.backend1.Entity.Bookdate;
import airbnb.com.backend1.Entity.Booking;
import airbnb.com.backend1.Entity.Discount;
import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Notify;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Enums.BookingStatus;
import airbnb.com.backend1.Entity.Enums.NotifyStatus;
import airbnb.com.backend1.Entity.Enums.Role;
import airbnb.com.backend1.Entity.Request.BookingRequest;
import airbnb.com.backend1.Entity.Response.BookingResponse;
import airbnb.com.backend1.Entity.Response.CountDiscoutResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.BookingMapper;
import airbnb.com.backend1.Repository.BookdateRepos;
import airbnb.com.backend1.Repository.BookingRepos;
import airbnb.com.backend1.Repository.DiscountRepos;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.NotifyRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.BookingService;

@Service 
public class BookingServiceIml implements BookingService {
    @Autowired
    BookingRepos bookingRepos;
    @Autowired
    BookingMapper bookingMapper;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HostRepos hostRepos;
    @Autowired
    BookdateRepos bookdateRepos;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    HomeServiceIml homeServiceIml;
    @Autowired
    DiscountRepos discountRepos;
    @Autowired
    NotifyRepos notifyRepos;
    
    @Override
    public BookingResponse createBooking(BookingRequest request) {
        Home home = getHome(request.getHomeId());
        Users authUser = getAuthUser();
        boolean validDateOfRequest = checkValidDateOfBooking(request.getCheckInDate(), request.getCheckOutDate());
        if(validDateOfRequest == false) {
            throw new BadResultException("the checkin date and checkout date are not valid");
        }

       boolean isValidDateOfBookingToHome = checkValidDateOfBookingToHome(home, request.getCheckInDate(), request.getCheckOutDate());
       if(isValidDateOfBookingToHome == false) {
        throw new BadResultException("the checkin date and checkout date are not valid");
        }

        boolean isAvailableForBooking = homeServiceIml.checkAvailabilityOfHome(home, request.getCheckInDate(), request.getCheckOutDate());
        if(isAvailableForBooking == false) {
            throw new BadResultException("the home is not available for booking");
        }

        if(request.getGuests() > home.getCapacity()) {
            throw new BadResultException("the number of guest exceeds the home capacity");
        }

        Long daysBetween = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        System.out.println("days between: " + daysBetween);
        if(daysBetween != request.getDays()) {
            throw new BadResultException("the numer of days is not valid");
        }

        Discount discount = home.getDiscount();
        System.out.println(discount);
        double priceAfterDiscount = 0;

        if(discount != null) {
             priceAfterDiscount = countTotalPriceAfterDiscount(home, discount, request.getCheckInDate(), request.getCheckOutDate());
            System.out.println(priceAfterDiscount);
        if(request.getPriceAfterDiscount() != null && request.getPriceAfterDiscount() != priceAfterDiscount) {
            throw new BadResultException("the price after discounted is wrong");
        }
        }
        System.out.println(priceAfterDiscount + " price after discount");

        double totalPrice = home.getPrice() * request.getDays();

        if( request.getTotalPrice() != totalPrice) {
            throw new BadResultException("the total price  is wrong");
        }
        
        String bookingCode = UUID.randomUUID().toString();
        System.out.println(bookingCode);
        Booking booking = new Booking(request.getGuests(), daysBetween, totalPrice, request.getCheckInDate(), request.getCheckOutDate(), true, bookingCode, home, authUser);
        System.out.println(booking);
        if(priceAfterDiscount != 0) {
            booking.setPriceAfterDiscount(Double.valueOf(priceAfterDiscount));
        } else {
            booking.setPriceAfterDiscount(totalPrice);
        }
        booking.setStatus(BookingStatus.PENDING);
        bookingRepos.save(booking);

        authUser.getBookings().add(booking);
        home.getBookings().add(booking);
        userRepos.save(authUser);
        homeRepos.save(home);

        Host host = home.getOwner();
        Notify notify = new Notify(NotifyStatus.PENDING_BOOKING, authUser, host, home, booking, false);
        notifyRepos.save(notify);
        
        authUser.getNotifies().add(notify);
        host.getNotifies().add(notify);
        home.getNotifies().add(notify);

        userRepos.save(authUser);
        homeRepos.save(home);
        hostRepos.save(host);

        createBookDateForBooking(booking, home, request.getCheckInDate(), request.getCheckOutDate());
        return bookingMapper.mapBookingToResponse(booking);
    }

    @Override
    public BookingResponse acceptBooking(Long bookingId) {
        Users authUser = getAuthUser();
        Host authHost = authUser.getHost();
        Booking booking = getBooking(bookingId);
        Home home = booking.getHome();
        Users tenant = booking.getTenant();

        if(authHost == null || home.getOwner().getId() != authHost.getId()) {
            throw new BadResultException("unAuthorized to modify booking due to not being the host of the home");
        }
        booking.setStatus(BookingStatus.ACCECPTED);
        bookingRepos.save(booking);

        Notify notify = new Notify(NotifyStatus.ACCECPTED_BOOKING, tenant, authHost, home, booking, false);
        notifyRepos.save(notify);

        tenant.getNotifies().add(notify);
        authHost.getNotifies().add(notify);
        home.getNotifies().add(notify);

        userRepos.save(tenant);
        homeRepos.save(home);
        hostRepos.save(authHost);

        return bookingMapper.mapBookingToResponse(booking);
    }

    @Override
    public BookingResponse unAcceptBooking(Long bookingId) {
        Users authUser = getAuthUser();
        Host authHost = authUser.getHost();
        Booking booking = getBooking(bookingId);
        Home home = booking.getHome();
        Users tenant = booking.getTenant();

        if(authHost == null || home.getOwner().getId() != authHost.getId()) {
            throw new BadResultException("unAuthorized to modify booking due to not being the host of the home");
        }
        booking.setStatus(BookingStatus.UNACCEPTED);
        deleteBookdatesByBooking(bookingId);
        bookingRepos.save(booking);

        Notify notify = new Notify(NotifyStatus.UNACCEPTED_BOOKING, tenant, authHost, home, booking, false);
        notifyRepos.save(notify);

        tenant.getNotifies().add(notify);
        authHost.getNotifies().add(notify);
        home.getNotifies().add(notify);

        userRepos.save(tenant);
        homeRepos.save(home);
        hostRepos.save(authHost);

        return bookingMapper.mapBookingToResponse(booking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Users authUser = getAuthUser();
        Booking booking = getBooking(bookingId);
        Home home = booking.getHome();
        Host host = home.getOwner();
        LocalDate currentTime = LocalDate.now();
        System.out.println("authuser " + authUser.getId() );
        System.out.println("tenant " + booking.getTenant().getId() );

         if(!currentTime.plusDays(14).isBefore(booking.getCheckInDate())) {
            throw new BadResultException("cannot cancel booking after 14 days");
        }

        if(authUser.getId() == booking.getTenant().getId() || authUser.getRoles().contains(Role.ADMIN)) {
            deleteBookdatesByBooking(bookingId);
            bookingRepos.delete(booking);

            Notify notify = new Notify(NotifyStatus.CANCEL_BOOKING, authUser, host, home,  false);
            notifyRepos.save(notify);

            authUser.getNotifies().add(notify);
            host.getNotifies().add(notify);
            home.getNotifies().add(notify);

            userRepos.save(authUser);
            homeRepos.save(home);
            hostRepos.save(host);
        } else {

            throw new BadResultException("unAuthorized to delete Booking");
        }  
    }
    
    @Override
    public BookingResponse getById(Long BookingId) {
       Booking booking = getBooking(BookingId);
       BookingResponse res = bookingMapper.mapBookingToResponse(booking);
       return res;

    }
  
    @Override
    public List<BookingResponse> getOldBookingsByTenant(Long tenantId) {
        LocalDate currentTime = LocalDate.now();
        Users tenant = getUserById(tenantId);
        List<Booking> bookings = bookingRepos.findByTenant(tenant);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isBefore(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        return res;
    }


    @Override
    public List<BookingResponse> getUpcomingBookingsByTenant(Long tenantId) {
        LocalDate currentTime = LocalDate.now();
        Users tenant = getUserById(tenantId);
        List<Booking> bookings = bookingRepos.findByTenant(tenant);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isAfter(currentTime) || booki.getCheckOutDate().isEqual(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        res.sort((a, b) -> b.getStatus().getName().compareTo(a.getStatus().getName()));
        return res;
    }
   
    @Override
    public List<BookingResponse> getUpcomingBookingsByHome(Long homeId) {
        LocalDate currentTime = LocalDate.now();
        Home home = getHome(homeId);
        List<Booking> bookings = bookingRepos.findByHome(home);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isAfter(currentTime) || booki.getCheckOutDate().isEqual(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        res.sort((a, b) -> b.getStatus().getName().compareTo(a.getStatus().getName()));
        return res;
    }
    @Override
    public List<BookingResponse> getOldBookingsByHome(Long homeId) {
        LocalDate currentTime = LocalDate.now();
        Home home = getHome(homeId);
        List<Booking> bookings = bookingRepos.findByHome(home);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isBefore(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        return res;
    }
    @Override
    public List<BookingResponse> getUpcomingBookingsByHost(Long hostId) {
        LocalDate currentTime = LocalDate.now();
        List<Booking> bookings = bookingRepos.findByHost(hostId);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isAfter(currentTime) || booki.getCheckOutDate().isEqual(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        res.sort((a, b) -> b.getStatus().getName().compareTo(a.getStatus().getName()));
        return res;

    }

    @Override
    public List<BookingResponse> getOldBookingsByHost(Long hostId) {
        LocalDate currentTime = LocalDate.now();
        List<Booking> bookings = bookingRepos.findByHost(hostId);
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isBefore(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        return res;
    }
   
    @Override
    public List<BookingResponse> getoldBookings() {
        LocalDate currentTime = LocalDate.now();
        List<Booking> bookings = bookingRepos.findAll();
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isBefore(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        return res;
    }
    
   
    @Override
    public List<BookingResponse> getUpcomingBookings() {
        LocalDate currentTime = LocalDate.now();
        List<Booking> bookings = bookingRepos.findAll();
        List<BookingResponse> res = bookings.stream().filter(booki -> booki.getCheckOutDate().isAfter(currentTime) || booki.getCheckOutDate().isEqual(currentTime)).map(booki -> bookingMapper.mapBookingToResponse(booki)).collect(Collectors.toList());
        res.sort((a, b) -> b.getStatus().getName().compareTo(a.getStatus().getName()));
        return res;
    }
  
    @Override
    public BookingResponse updateBooking(Long bookingId, LocalDate checkInDate, LocalDate checkOutDate, Long days,  Double totalPrice) {
       Users authUser = getAuthUser();
       
       Booking booking = getBooking(bookingId);
       Home home = booking.getHome();
       Host host = home.getOwner();

       if(authUser.getId() != booking.getTenant().getId()) {
        throw new BadResultException("unAuthorized to update Booking");
       }
                
       deleteBookdatesByBooking(bookingId);

       boolean validDateOfRequest = checkValidDateOfBooking(checkInDate, checkOutDate);
        if(validDateOfRequest == false) {
            throw new BadResultException("the checkin date and checkout date are not valid");
        }

       boolean isValidDateOfBookingToHome = checkValidDateOfBookingToHome(home, checkInDate, checkOutDate);
       if(isValidDateOfBookingToHome == false) {
        throw new BadResultException("the checkin date and checkout date are not valid");
    }

        boolean isAvailableForBooking = homeServiceIml.checkAvailabilityOfHome(home, checkInDate, checkOutDate);
        if(isAvailableForBooking == false) {
            throw new BadResultException("the home is not available for booking");
        }

        Long daysBetween = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        System.out.println(daysBetween);
        if(daysBetween != days) {
            throw new BadResultException("the number of days is not valid");
        }

        Discount discount = home.getDiscount();
        
        double priceAfterDiscount = countTotalPriceAfterDiscount(home, discount, checkInDate, checkOutDate);


        double totalPriceReq = home.getPrice() * days;

        if( totalPriceReq != totalPrice) {
            throw new BadResultException("the total price  is wrong");
        }

        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(totalPriceReq);
        booking.setDays(daysBetween);
        booking.setPriceAfterDiscount(priceAfterDiscount);
        bookingRepos.save(booking);
        createBookDateForBooking(booking, home, checkInDate, checkOutDate);

        
        Notify notify = new Notify(NotifyStatus.UPDATE_BOOKING, authUser, host, home, booking, false);
        notifyRepos.save(notify);
        
        authUser.getNotifies().add(notify);
        host.getNotifies().add(notify);
        home.getNotifies().add(notify);

        userRepos.save(authUser);
        homeRepos.save(home);
        hostRepos.save(host);

       return bookingMapper.mapBookingToResponse(booking);
    }

    @Override
    public CountDiscoutResponse getCountDiscount(Long homeId, LocalDate checkin, LocalDate checkout) {
      Home home = getHome(homeId);
      Discount discount = home.getDiscount();

    Double priceAfterDiscount = countTotalPriceAfterDiscount(home, discount, checkin, checkout);
    Long days = ChronoUnit.DAYS.between(checkin, checkout);
    double totalPrice = days * home.getPrice();
    CountDiscoutResponse res = new CountDiscoutResponse(totalPrice - priceAfterDiscount, priceAfterDiscount, totalPrice, homeId, checkin, checkout, days);
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

    private Users getUserById(Long id) {
       
        Optional<Users> entity = userRepos.findById(id);
        Users user = isCheck(entity);
        return user;
    }

    private Host getHostById(Long id) {
        Optional<Host> entity = hostRepos.findById(id);
        if(entity.isPresent()) {
            return entity.get();
        }
        throw new EntityNotFoundException("the host not found");
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

    private boolean checkValidDateOfBooking(LocalDate checkin, LocalDate checkout) {
      
        LocalDate currentTime = LocalDate.now();
        if(checkin.isEqual(currentTime) && checkout.isAfter(currentTime)) {
            return true;
        }
        if(checkin.isAfter(currentTime) && checkout.isAfter(checkin)) {
            return true;
        }
        return false;
    }

    private boolean isBetweenTwoDates(LocalDate date, LocalDate openDate, LocalDate closeDate) {
        if(date.isBefore(openDate) || date.isAfter(closeDate)) {
            return false;
        }
      
        return true;
    }

    private Double countTotalPriceAfterDiscount(Home home, Discount discount,  LocalDate checkin, LocalDate checkout) {
        LocalDate checkInDate = checkin;
        LocalDate checkOutDate = checkout.minusDays(1);
        Double totalPrice = 0.00;
        // System.out.println(totalPrice);

        if(!checkin.isEqual(checkOutDate)) {
            // System.out.println("not equal");
            while(!checkInDate.isAfter(checkOutDate)) {
                boolean isBetween = isBetweenTwoDates(checkInDate,  discount.getOpenDate(), discount.getCloseDate());
                if(isBetween) {
                    totalPrice +=  home.getPrice() - (home.getPrice() * discount.getDiscountRate() / 100) ;
                } else {
                    totalPrice += home.getPrice();
                }
               checkInDate = checkInDate.plusDays(1);
                // System.out.println(checkInDate);
            }
        } else {
            boolean isBetween = isBetweenTwoDates(checkInDate,  discount.getOpenDate(), discount.getCloseDate());
            if(isBetween) {
                return home.getPrice() * discount.getDiscountRate() / 100;
            } else {
                return home.getPrice();
            }
        }
        System.out.println(totalPrice);
        return totalPrice;
    }

    private boolean checkValidDateOfBookingToHome(Home home, LocalDate checkin, LocalDate checkout) {
      
        LocalDate currentTime = LocalDate.now();
       if(checkin.isBefore(home.getOpenBooking()) || checkout.isAfter(home.getCloseBooking())) {
        return false;
       }
    //    if(checkout.isAfter(home.getCloseBooking())) {
    //     return false;
    //    }
       return true;
    }
    private List<Bookdate> getBookdatesByBooking(Long bookingId) {
        Booking booking = getBooking(bookingId);
        List<Bookdate> dates = bookdateRepos.findByBooking(booking);
       return dates;
    }
    private void deleteBookDate(Long bookDateId) {
        bookdateRepos.deleteById(bookDateId);
    }

    private void deleteBookdatesByBooking(Long bookingId) {
        List<Bookdate> bookdates = getBookdatesByBooking(bookingId);
        if(!bookdates.isEmpty()) {
            bookdates.stream().forEach(bookdate -> deleteBookDate(bookdate.getId()));
        }

    }

    // private Bookdate createBookDate(LocalDate date, Booking booking, Home home) {
    //     Bookdate bookdate = new Bookdate(date, booking, home);
    //     return bookdateRepos.save(bookdate);
    // }

    private void createBookDateForBooking(Booking booking, Home home, LocalDate checkIn, LocalDate checkOut) {
        LocalDate checkInDate = checkIn;
        LocalDate checkOutDate = checkOut.minusDays(1);
    if(!checkIn.isEqual(checkOut.minusDays(1))) {
        while(!checkInDate.isAfter(checkOutDate)) {
            Bookdate bookdate = new Bookdate(checkInDate, booking, home);
            bookdateRepos.save(bookdate);
            booking.getBookdates().add(bookdate);
            home.getBookdates().add(bookdate);
            checkInDate = checkInDate.plusDays(1);
            
        }
    } else {
        Bookdate bookdate = new Bookdate(checkIn, booking, home);
        bookdateRepos.save(bookdate);
        booking.getBookdates().add(bookdate);
        home.getBookdates().add(bookdate);
    }
    bookingRepos.save(booking);
    homeRepos.save(home);
    }

}
