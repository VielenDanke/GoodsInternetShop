package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    public static final String WILDCARD = "%";
    public static final String USERNAME_FOR_SEARCH = "test";
    public static final String ADDRESS_FOR_SEARCH = "Караганда";
    public static final String ADDRESS_FOR_UPDATE = "Москва, ул. Петроградская 78-44";
    public static final String FULLNAME_FOR_SEARCH = "Аманболов";
    public static final Set<Role> ROLES =  new HashSet<>(Arrays.asList(Role.ROLE_USER));

    public static final User NEW_USER = User.builder()
                    .username("newuser")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Павлодар, ул. Чкалова 33-08")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Исаев Павел Михайлович").build();

    public static final List<User> USERS = Arrays.asList(
            User.builder()
                    .username("testA")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, ул. Гоголя 36-8")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Аманболов Нуркен Касымулы").build(),
            User.builder()
                    .username("testB")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Караганда, пр. Бухар Жырау 3-28")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Михайлов Игорь Анатольевич").build(),
            User.builder()
                    .username("testC")
                    .password("test")
                    .gender("test")
                    .locale("test")
                    .address("Астана, ул. Победы 45-16")
                    .enabled(1)
                    .authority(ROLES)
                    .fullName("Аманболов Айнур Бериккызы").build()
    );

    @Before
    public void setUp() {
        userRepository.deleteAll();
        USERS.forEach(user -> {
                    user.setId(null);
                    userRepository.save(user);
                }
        );
    }

    @Test
    @Transactional
    @Rollback
    public void findUserByUsername() {
        User expectedUser = USERS.get(0);
        User actualUser = userRepository.findByUsername(expectedUser.getUsername()).get();
        Assert.assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
    @Transactional
    @Rollback
    public void findByUsernameLike() {
        List<User> actualUserList = userRepository.findByUsernameLike(wrapWithWildcard(USERNAME_FOR_SEARCH));
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getUsername().contains(USERNAME_FOR_SEARCH))
                .collect(Collectors.toList());
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }

    @Test
    @Transactional
    @Rollback
    public void findByAddressLike() {
        List<User> actualUserList = userRepository.findByAddressLike(wrapWithWildcard(ADDRESS_FOR_SEARCH));
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getAddress().contains(ADDRESS_FOR_SEARCH))
                .collect(Collectors.toList());
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }

    @Test
    @Transactional
    @Rollback
    public void findByFullNameLike() {
        List<User> actualUserList = userRepository.findByFullNameLike(wrapWithWildcard(FULLNAME_FOR_SEARCH));
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getFullName().contains(FULLNAME_FOR_SEARCH))
                .collect(Collectors.toList());
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }

    @Test
    @Transactional
    @Rollback
    public void createUser() {
        userRepository.save(NEW_USER);
        assertThat(userRepository.existsById(NEW_USER.getId())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void updateUser() {
        User user = USERS.get(0);
        user.setAddress(ADDRESS_FOR_UPDATE);
        userRepository.save(user);
        String actualAddress = userRepository.findById(user.getId()).get().getAddress();
        Assert.assertEquals(ADDRESS_FOR_UPDATE, actualAddress);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteUserById() {
        User user = USERS.get(0);
        long userId = user.getId();
        userRepository.deleteById(userId);
        assertThat(userRepository.existsById(userId)).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByUsername() {
        User user = USERS.get(0);
        String userName = user.getUsername();
        userRepository.deleteByUsername(userName);
        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    private String wrapWithWildcard(String str) {
        return WILDCARD + str + WILDCARD;
    }
}
