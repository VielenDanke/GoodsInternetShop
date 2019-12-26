package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.CRUDInterface;

import java.util.List;
import java.util.Optional;

public interface UserService extends CRUDInterface<User> {

    User findById(Long id);

    void deleteAll();

    List<User> findByUsernameLike(String usernameLike);

    List<User> findByAddressLike(String addressLike);

    List<User> findByFullNameLike(String fullNameLike);

    void deleteByUsername(String username);

    List<User> findAll();

    Optional<User> findByUsername(String username);
}
