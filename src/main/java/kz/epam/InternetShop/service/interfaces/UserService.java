package kz.epam.InternetShop.service.interfaces;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.service.CRUDInterface;

public interface UserService<E extends User> extends CRUDInterface<User> {
}
