package airbnb.com.backend1.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import airbnb.com.backend1.Entity.Image;

@Repository
public interface ImageRepos extends JpaRepository<Image, Long> {
    Optional<Image> findByFileName(String fileName);
}
