package airbnb.com.backend1.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import airbnb.com.backend1.Repository.UserRepos;
import airbnb.com.backend1.Security.Filter.FilterAuthentication;
import airbnb.com.backend1.Security.Filter.FilterException;
import airbnb.com.backend1.Security.Filter.FilterJwtAuthorization;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
public class SecurityConfig {
    @Autowired
    CustomAuthenticationManager customAuthenticationManager;
    @Autowired
    UserRepos userRepos;
    @Autowired
    FilterException filterException;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    LogoutHandler logoutHandler;
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        FilterAuthentication filterAuthentication = new FilterAuthentication(customAuthenticationManager, userRepos);
        filterAuthentication.setFilterProcessesUrl("/api/users/login");

        http.csrf().disable()
        .cors()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/users/forgotPassword/**").permitAll()
        .requestMatchers( "/api/images/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/all").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/search").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/city/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/category/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/home/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homes/host/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/countries/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/cities/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homeCategories/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/hosts/host/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/homeReviews/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/wishlists/home/**").permitAll()
        .requestMatchers(HttpMethod.GET ,"/api/tenantReviews/tenant/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(filterException, filterAuthentication.getClass())
        .addFilter(filterAuthentication)
        .addFilterAfter(new FilterJwtAuthorization(), filterAuthentication.getClass());

        http.logout().permitAll()
        .addLogoutHandler((request, response, auth) -> {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setHeader("Authorization", "");
        })
        .logoutSuccessHandler(logoutHandler);

        http.cors();
        return http.build();
    }


   
}
