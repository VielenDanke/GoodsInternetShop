package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.util.UserTestDataTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private static final String WILDCARD = "%";

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
        List<User> actualUserList = userRepository.findByFullNameLike(wrapWithWildcard(FULL_NAME_FOR_SEARCH));
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getFullName().contains(FULL_NAME_FOR_SEARCH))
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