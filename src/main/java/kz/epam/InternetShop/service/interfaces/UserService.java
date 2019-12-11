package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.CRUDInterface;

import java.util.List;

public interface UserService<E extends User> extends CRUDInterface<User> {

    List<E> findByUsernameLike(String usernameLike);

    List<E> findByAddressLike(String addressLike);

    List<E> findByFullNameLike(String fullNameLike);

    void deleteByUsername(String username);
}
