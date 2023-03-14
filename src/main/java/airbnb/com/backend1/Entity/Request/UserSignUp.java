package airbnb.com.backend1.Entity.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUp {
    private String username;
    private String email;
    private String imageurl;
    private String password;
    private String confirmPassword;

    public UserSignUp(String username, String email,  String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
   
}
