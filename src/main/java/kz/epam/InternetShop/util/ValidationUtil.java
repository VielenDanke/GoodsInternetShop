package kz.epam.InternetShop.util;

import kz.epam.InternetShop.util.exception.NotFoundException;

public class ValidationUtil {

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }
}
