package airbnb.com.backend1.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Home;
import airbnb.com.backend1.Entity.Host;
import airbnb.com.backend1.Entity.TenantReview;
import airbnb.com.backend1.Entity.Users;

@Repository
public interface TenantReviewRepos extends JpaRepository<TenantReview, Long> {
    List<TenantReview> findByTenant(Users tenant);
    Optional<TenantReview> findByTenantAndHost(Users tenant, Host host);

}
