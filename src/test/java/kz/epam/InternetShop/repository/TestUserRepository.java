package kz.epam.InternetShop.repository;

import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        user = User.builder()
                .username("test")
                .password("test")
                .gender("test")
                .locale("test")
                .address("test")
                .enabled(1)
                .authority(Collections.singleton(Role.ROLE_USER))
                .fullName("test").build();
    }

    @Test
    public void testUserRepositoryIsNotNull() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void shouldFindUserByUsername() {
        assertThat(userRepository.findByUsername("User")).isNotNull();
    }

    @Test
    @Rollback
    @Transactional
    public void shouldSaveUserInDatabase() {
        userRepository.save(user);
        assertThat(userRepository.findByUsername("test")).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldDeleteUserById() {
        userRepository.deleteById((long) 100001);
        assertThat(userRepository.existsById((long) 100001)).isFalse();
    }
}
