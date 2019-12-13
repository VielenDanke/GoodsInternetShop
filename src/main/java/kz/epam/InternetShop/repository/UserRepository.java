package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByUsernameLike(String usernameLike);

    List<User> findByAddressLike(String addressLike);

    List<User> findByFullNameLike(String fullNameLike);

    void deleteByUsername(String username);
}
