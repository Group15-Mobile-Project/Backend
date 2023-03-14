package airbnb.com.backend1.Security.Filter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import airbnb.com.backend1.Security.SecurityConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FilterJwtAuthorization extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith(SecurityConstant.authorization)) {
          filterChain.doFilter(request, response);
          return;
        }
        String token = authorization.replace(SecurityConstant.authorization, "");
      System.out.println(token);
      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstant.private_key)).build().verify(token);
      Date currentTime = new Date(System.currentTimeMillis());
      Date tokenTime = decodedJWT.getExpiresAt();
      if(currentTime.after(tokenTime)) {
          throw new JWTVerificationException("token time expires");
      }
  
      String username = decodedJWT.getSubject();
      System.out.println(username);
      List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("authorities").asList(String.class).stream().map(cla -> new SimpleGrantedAuthority(cla)).collect(Collectors.toList());
      System.out.println(authorities);
      Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      filterChain.doFilter(request, response);
    }
}
