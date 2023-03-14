package airbnb.com.backend1.Entity.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSignUp {
    private String username;
    private String email;
    private String imageurl;
    private String password;
    private String confirmPassword;
    private String adminKey;

    public AdminSignUp(String username, String email, String password, String confirmPassword, String adminKey) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.adminKey = adminKey;
    }

   
}
