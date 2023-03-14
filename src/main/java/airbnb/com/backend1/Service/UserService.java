package airbnb.com.backend1.Service;

import java.util.List;

import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Request.AdminSignUp;
import airbnb.com.backend1.Entity.Request.ChangePassword;
import airbnb.com.backend1.Entity.Request.UserSignUp;
import airbnb.com.backend1.Entity.Response.UserResponse;

public interface UserService {
    UserResponse saveUser(UserSignUp userSignup);
    UserResponse saveAdmin(AdminSignUp adminSignup);
    List<UserResponse> getListUsers();
    List<UserResponse> getListUsersByName(String username);
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
  
    UserResponse updatePassword(ChangePassword changePassword);
    void deleteUsers(Long id);
 
    UserResponse updateToAdmin(Long userId);
    UserResponse updateToHost();
}
