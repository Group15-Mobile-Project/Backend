package airbnb.com.backend1.Service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Enums.Role;
import airbnb.com.backend1.Entity.Request.AdminSignUp;
import airbnb.com.backend1.Entity.Request.ChangePassword;
import airbnb.com.backend1.Entity.Request.UserSignUp;
import airbnb.com.backend1.Entity.Response.UserResponse;
import airbnb.com.backend1.Exception.BadResultException;
import airbnb.com.backend1.Exception.EntityExistingException;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Mapper.UserMapper;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Security.SecurityConstant;
import airbnb.com.backend1.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
public class UserServiceIml implements UserDetailsService, UserService {
    @Autowired
    UserRepos userRepos;
    @Autowired
    UserMapper userMapper;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HostRepos hostRepos;
    @Value("${admin.key}")
    private String adminKey;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<Users> entity = userRepos.findByUsername(username);
        if(!entity.isPresent()) {
            throw new EntityNotFoundException("the user not found");
        }
        Users user = entity.get();
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        User userDetail = new User(user.getUsername(), user.getPassword(), authorities);
        return userDetail;
    }

    @Override
    public void deleteUsers(Long id) {
        Optional<Users> entity = userRepos.findById(id);
        Users user = isCheck(entity);
        userRepos.delete(user);
        
    }

    @Override
    public List<UserResponse> getListUsers() {
        List<Users> users = userRepos.findAll(Sort.by(Sort.Direction.ASC, "username"));
        List<UserResponse> userResponses = users.stream().map(user -> userMapper.mapUserToResponse(user)).collect(Collectors.toList());
        return userResponses;
    }

    @Override
    public List<UserResponse> getListUsersByName(String username) {
        List<Users> users = userRepos.findByUsernameContaining( username);
        users.sort((a, b) -> a.getUsername().compareTo(b.getUsername()));
        List<UserResponse> userResponses = users.stream().map(user -> userMapper.mapUserToResponse(user)).collect(Collectors.toList());
        return userResponses;
    }

   

    @Override
    public UserResponse getUserById(Long id) {
        Optional<Users> entity = userRepos.findById(id);
        Users user = isCheck(entity);
        UserResponse userResponse = userMapper.mapUserToResponse(user);
        return userResponse;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        Optional<Users> entity = userRepos.findByUsername(username);
        Users user = isCheck(entity);
        UserResponse userResponse = userMapper.mapUserToResponse(user);
        return userResponse;
    }

    @Override
    public UserResponse saveUser(UserSignUp userSignup) {
        if(!userSignup.getPassword().equals(userSignup.getConfirmPassword())) {
            throw new BadResultException("the passwords don't match");
        }

        Optional<Users> usernameEntity = userRepos.findByUsername(userSignup.getUsername());
        if(usernameEntity.isPresent()) {
            throw new EntityExistingException("the username exist");
        }

        Optional<Users> emailEntity = userRepos.findByEmail(userSignup.getEmail());
        if(emailEntity.isPresent()) {
            throw new EntityExistingException("the email exist");
        }

       Users user = new Users(userSignup.getUsername(), userSignup.getEmail(), new BCryptPasswordEncoder().encode(userSignup.getConfirmPassword()));
       if(userSignup.getImageurl() != null) {
        user.setImageurl(userSignup.getImageurl());
       }
       user.getRoles().add(Role.USER);
       userRepos.save(user);
       List<String> claims = user.getRoles().stream().map(ro -> ro.getName()).collect(Collectors.toList());
       
       String token = JWT.create()
       .withSubject(user.getUsername())
       .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.expire_time))
       .withClaim("authorities", claims)
       .sign(Algorithm.HMAC512(SecurityConstant.private_key));
       
        response.setHeader("Authorization", SecurityConstant.authorization + token);

        return userMapper.mapUserToResponse(user);
    }

  @Override
  public UserResponse saveAdmin(AdminSignUp adminSignup) {

    if(!adminSignup.getAdminKey().equals(adminKey)) {
        throw new BadResultException("are not authorized to create admin account");
    }

    if(!adminSignup.getPassword().equals(adminSignup.getConfirmPassword())) {
        throw new BadResultException("the passwords don't match");
    }

    Optional<Users> usernameEntity = userRepos.findByUsername(adminSignup.getUsername());
    if(usernameEntity.isPresent()) {
        throw new EntityExistingException("the username exist");
    }

    Optional<Users> emailEntity = userRepos.findByEmail(adminSignup.getEmail());
    if(emailEntity.isPresent()) {
        throw new EntityExistingException("the email exist");
    }

   Users user = new Users(adminSignup.getUsername(), adminSignup.getEmail(), new BCryptPasswordEncoder().encode(adminSignup.getConfirmPassword()));
   if(adminSignup.getImageurl() != null) {
    user.setImageurl(adminSignup.getImageurl());
   }
//    user.getRoles().add(Role.USER);
   user.getRoles().add(Role.ADMIN);
   userRepos.save(user);
   List<String> claims = user.getRoles().stream().map(ro -> ro.getName()).collect(Collectors.toList());
   
   String token = JWT.create()
   .withSubject(user.getUsername())
   .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.expire_time))
   .withClaim("authorities", claims)
   .sign(Algorithm.HMAC512(SecurityConstant.private_key));
   
    response.setHeader("Authorization", SecurityConstant.authorization + token);

    return userMapper.mapUserToResponse(user);
  }

    @Override
    public UserResponse updatePassword(ChangePassword changePassword) {
        Users user = getAuthUser();
        if(!new BCryptPasswordEncoder().matches(changePassword.getCurrentPassword(), user.getPassword())) {
            throw new BadResultException("the current password is wrong");
        }
        if(!changePassword.getNewPassword().equals(changePassword.getNewPassword())) {
            throw new BadResultException("the new and confirmed password are wrong");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(changePassword.getNewPassword()));
        userRepos.save(user);
        List<String> claims = user.getRoles().stream().map(ro -> ro.getName()).collect(Collectors.toList());
     
        
        String token = JWT.create()
        .withSubject(user.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.expire_time))
        .withClaim("claims", claims)
        .sign(Algorithm.HMAC512(SecurityConstant.private_key));
        
         response.setHeader("Authorization", SecurityConstant.authorization + token);
 
        return userMapper.mapUserToResponse(user);
    }

    @Override
    public UserResponse updateToAdmin(Long userId) {
        Optional<Users> entity = userRepos.findById(userId);
        Users user = isCheck(entity);
        if(user.getRoles().contains(Role.ADMIN)) {
            throw new BadResultException("the user has the admin role, cannot update");
        }
        user.getRoles().add(Role.ADMIN);
        userRepos.save(user);
        return userMapper.mapUserToResponse(user);
    }

    @Override
    public UserResponse updateToHost() {
        
        Users user = getAuthUser();
        if(user.getRoles().contains(Role.HOST)) {
            throw new BadResultException("the user has the host role, cannot update");
        }
        user.getRoles().add(Role.HOST);

        Host host = new Host(user);
        user.setHost(host);
        user.setIsHost(true);
        hostRepos.save(host);
        userRepos.save(user);

        return userMapper.mapUserToResponse(user);
    }

    @Override
    public UserResponse updateUserProfile(String username, String email, String imageurl) {
        Users user = getAuthUser();
        if(username != null && !username.equals(user.getUsername())) {
            Optional<Users> usernameEntity = userRepos.findByUsername(username);
            if(usernameEntity.isPresent() ) {
                throw new EntityExistingException("the username exists");
            } 
            user.setUsername(username);
        }
        if(email != null && !email.equals(user.getEmail())) {
            Optional<Users> usernameEntity = userRepos.findByUsername(email);
            if(usernameEntity.isPresent() ) {
                throw new EntityExistingException("the email exists");
            } 
            user.setEmail(email);
        }
        if(imageurl != null) {
            user.setImageurl(imageurl);
        }
        userRepos.save(user);
        return userMapper.mapUserToResponse(user);
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
