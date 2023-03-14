package airbnb.com.backend1.Security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Exception.EntityNotFoundException;
import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Service.implementation.UserServiceIml;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {
    @Autowired
    private    UserRepos userRepos;
    @Autowired
    UserServiceIml userServiceImp;
   

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // String username = authentication.getName();
        // Optional<Users> entity = userRepos.findByUsername(username);
        // if(!entity.isPresent()) {
        //     throw new EntityNotFoundException("the user not exist");
        // }
       
        // Users user = entity.get();
        // boolean isCheck = new BCryptPasswordEncoder().matches(authentication.getCredentials().toString(), user.getPassword());
        // if(isCheck == false) {
        //     throw new BadCredentialsException("password provided is wrong");
        // }
        // List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        // System.out.println(authorities);
        // System.out.println("authorities");
        // return new UsernamePasswordAuthenticationToken(username, user.getPassword(), authorities);
        String username = authentication.getName();
        UserDetails user = userServiceImp.loadUserByUsername(username);
        if(!new BCryptPasswordEncoder().matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("password prodvided is wrong");
        }

        Authentication authentication2 = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
        return authentication2;
    }
}
