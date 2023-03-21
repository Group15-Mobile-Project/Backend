package airbnb.com.backend1.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import airbnb.com.backend1.Entity.Request.AdminSignUp;
import airbnb.com.backend1.Entity.Request.ChangePassword;
import airbnb.com.backend1.Entity.Request.UserSignUp;
import airbnb.com.backend1.Entity.Response.UserResponse;
import airbnb.com.backend1.Service.UserService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    //admin access
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAll() {
        return new ResponseEntity<>(userService.getListUsers(), HttpStatus.OK);
    }
    
    //admin access
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/searchByName/{name}")
    public ResponseEntity<List<UserResponse>> getAllByName(@PathVariable String name) {
        return new ResponseEntity<>(userService.getListUsersByName(name), HttpStatus.OK);
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> Register(@Valid @RequestBody UserSignUp userSignUp) {
    
        return new ResponseEntity<>(userService.saveUser(userSignUp), HttpStatus.OK);
    }
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserResponse> RegisterAdmin(@Valid @RequestBody AdminSignUp adminSignUp) {
    
        return new ResponseEntity<>(userService.saveAdmin(adminSignUp), HttpStatus.OK);
    }
    @PutMapping("/changePassword")
    public ResponseEntity<UserResponse> updatePassword(@Valid @RequestBody ChangePassword changePasswordRequest) {
        return new ResponseEntity<>(userService.updatePassword(changePasswordRequest), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateToAdmin/{id}")
    public ResponseEntity<UserResponse> updateAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(userService.updateToAdmin(id), HttpStatus.OK);
    }

    @PutMapping("/updateToHost")
    public ResponseEntity<UserResponse> updateHost() {
        return new ResponseEntity<>(userService.updateToHost(), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        userService.deleteUsers(userId);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
}

