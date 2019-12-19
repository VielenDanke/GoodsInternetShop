package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestFieldsUtil {

    public static final Random RANDOM_NUMBER_RANGE_999 = new Random(999);
    public static final Integer STATUS_IS_ONE = 1;
    public static final Integer ZERO_STATUS = 0;
    public final static String EXAMPLE_STRING = "example";
    public static final String USERNAME_FOR_SEARCH = "test";
    public static final String ADDRESS_FOR_SEARCH = "Караганда";
    public static final String ADDRESS_FOR_UPDATE = "Москва, ул. Петроградская 78-44";
    public static final String FULL_NAME_FOR_SEARCH = "Аманболов";
    public static final Set<GrantedAuthority> ROLES =  new HashSet<>(Arrays.asList(Role.ROLE_USER));
}
