package airbnb.com.backend1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.Users;
import airbnb.com.backend1.Entity.Enums.Role;
import airbnb.com.backend1.Repository.HostRepos;
import airbnb.com.backend1.Repository.UserRepos;

@SpringBootApplication
public class Backend1Application {

	public static void main(String[] args) {
		SpringApplication.run(Backend1Application.class, args);
	}
	// @Bean
	// CommandLineRunner commandLineRunner(UserRepos userRepos, HostRepos hostRepos) {
	// 	return args -> {
	// 		List<Role> adminRoles = new ArrayList<>();
	// 		adminRoles.add(Role.ADMIN);

	// 		Users admin = new Users("admin", "admin@gmail.com", new BCryptPasswordEncoder().encode("123456"), adminRoles);
	// 		userRepos.save(admin);

	// 		Host host = new Host(admin);
	// 		hostRepos.save(host);
	// 	};
	// }
}
