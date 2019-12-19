package kz.epam.InternetShop.util;

import kz.epam.InternetShop.model.User;

import java.util.Arrays;
import java.util.List;

public class UserTestDataTestUtil {

    public static final User NEW_USER = User.builder()
            .username("newuser@mail.ru")
            .password("test")
            .gender("test")
            .locale("test")
            .address("Павлодар, ул. Чкалова 33-08")
            .enabled(1)
            .authority(TestFieldsUtil.ROLES)
            .fullName("Исаев Павел Михайлович").build();

    public static final List<User> USERS = Arrays.asList(
            User.builder()
                    .username("testA@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, ул. Гоголя 36-8")
                    .enabled(1)
                    .authority(TestFieldsUtil.ROLES)
                    .fullName("Аманболов Нуркен Касымулы").build(),
            User.builder()
                    .username("testB@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, пр. Бухар Жырау 3-28")
                    .enabled(1)
                    .authority(TestFieldsUtil.ROLES)
                    .fullName("Михайлов Игорь Анатольевич").build(),
            User.builder()
                    .username("testC@mail.ru")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Астана, ул. Победы 45-16")
                    .enabled(1)
                    .authority(TestFieldsUtil.ROLES)
                    .fullName("Аманболов Айнур Бериккызы").build()
    );

}
