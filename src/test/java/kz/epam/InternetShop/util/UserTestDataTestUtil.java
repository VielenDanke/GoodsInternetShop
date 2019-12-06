package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserTestDataTestUtil {
    public static final String USERNAME_FOR_SEARCH = "test";
    public static final String ADDRESS_FOR_SEARCH = "Караганда";
    public static final String ADDRESS_FOR_UPDATE = "Москва, ул. Петроградская 78-44";
    public static final String FULL_NAME_FOR_SEARCH = "Аманболов";
    public static final Set<Role> ROLES =  new HashSet<>(Arrays.asList(Role.ROLE_USER));

    public static final User NEW_USER = User.builder()
            .username("newuser@mail.ru")
            .password("test")
            .gender("test")
            .locale("test")
            .address("Павлодар, ул. Чкалова 33-08")
            .enabled(1)
            .authority(ROLES)
            .fullName("Исаев Павел Михайлович").build();

    public static final List<User> USERS = Arrays.asList(
            User.builder()
                    .username("testA@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, ул. Гоголя 36-8")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Аманболов Нуркен Касымулы").build(),
            User.builder()
                    .username("testB@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, пр. Бухар Жырау 3-28")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Михайлов Игорь Анатольевич").build(),
            User.builder()
                    .username("testC@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Астана, ул. Победы 45-16")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Аманболов Айнур Бериккызы").build()
    );

}
