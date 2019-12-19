package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.interfaces.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static kz.epam.InternetShop.service.impl.UserServiceImpl.wrapWithWildcard;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.*;
import static kz.epam.InternetShop.util.UserTestDataTestUtil.FULL_NAME_FOR_SEARCH;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")

public class UserServiceIT {

    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @Test
    public void findByUsernameLike() {
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getUsername().contains(USERNAME_FOR_SEARCH))
                .collect(Collectors.toList());
        Mockito.when(repository.findByUsernameLike(wrapWithWildcard(USERNAME_FOR_SEARCH))).thenReturn(expectedUserList);
        List<User> actualUserList = userService.findByUsernameLike(USERNAME_FOR_SEARCH);
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }

    @Test
    public void findByAddressLike() {
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getAddress().contains(ADDRESS_FOR_SEARCH))
                .collect(Collectors.toList());
        Mockito.when(repository.findByAddressLike(wrapWithWildcard(ADDRESS_FOR_SEARCH))).thenReturn(expectedUserList);
        List<User> actualUserList = userService.findByAddressLike(ADDRESS_FOR_SEARCH);
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }

    @Test
    public void findByFullNameLike() {
        List<User> expectedUserList = USERS
                .stream()
                .filter(user -> user.getFullName().contains(FULL_NAME_FOR_SEARCH))
                .collect(Collectors.toList());
        Mockito.when(repository.findByFullNameLike(wrapWithWildcard(FULL_NAME_FOR_SEARCH))).thenReturn(expectedUserList);
        List<User> actualUserList = userService.findByFullNameLike(FULL_NAME_FOR_SEARCH);
        Assert.assertEquals(expectedUserList.size(), actualUserList.size());
        Assert.assertEquals(true, expectedUserList.containsAll(actualUserList));
    }
}
