package airbnb.com.backend1.Service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Wishlist;
import airbnb.com.backend1.Entity.Response.WishlistResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.WishlistMapper;
import airbnb.com.backend1.Repository.HomeRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Repository.WishlistRepos;
import airbnb.com.backend1.Service.WishlistService;

@Service
public class WishlistServiceIml implements WishlistService {
    @Autowired
    WishlistRepos wishlistRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    HomeRepos homeRepos;
    @Autowired
    WishlistMapper wishlistMapper;
    @Override
    public void delete(Long id) {
        Optional<Wishlist> entityWish = wishlistRepos.findById(id);
        if(!entityWish.isPresent()) {
            throw new EntityNotFoundException("the wishlist not found");
        } 
        wishlistRepos.deleteById(id);
    }
    @Override
    public List<WishlistResponse> getWishlistsByHome(Long homeId) {
       Home home = getHome(homeId);
       List<Wishlist> lists = wishlistRepos.findByHome(homeId);
       List<WishlistResponse> res = lists.stream().map(list -> wishlistMapper.mapWishlistToResponse(list)).collect(Collectors.toList());
       return res;
    }
    @Override
    public List<WishlistResponse> getWishlistsByAuthUser() {
        Users authUsers = getAuthUser();
       List<Wishlist> lists = wishlistRepos.findByHome(authUsers.getId());
       List<WishlistResponse> res = lists.stream().map(list -> wishlistMapper.mapWishlistToResponse(list)).collect(Collectors.toList());
       return res;
    }
    @Override
    public WishlistResponse save(Long homeId, Long userId) {
        Optional<Users> entity = userRepos.findById(userId);
        Users user = isCheck(entity);
        Users authUser = getAuthUser();
        if(user.getId() != authUser.getId()) {
            throw new BadResultException("are not authorized to add wishlist");
        }
        Home home = getHome(homeId);
        Optional<Wishlist> entityWish = wishlistRepos.findByHomeAndUser(home, user);
        if(entityWish.isPresent()) {
            throw new EntityExistingException("the wishlist exists");
        } 
        Wishlist wish = new Wishlist( authUser, home);
        wishlistRepos.save(wish);
        WishlistResponse res = wishlistMapper.mapWishlistToResponse(wish);
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
}
