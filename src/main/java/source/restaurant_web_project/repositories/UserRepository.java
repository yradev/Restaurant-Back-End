package source.restaurant_web_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import source.restaurant_web_project.models.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByEmail(String email);
    List<User> findUsersByEmailContainingOrderByEmail(String Email);
    List<User> findAllByOrderByEmail();
}
