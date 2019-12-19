package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService<User> {
    private static final String WILDCARD = "%";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findByUsernameLike(String usernameLike) {
        return userRepository.findByUsernameLike(wrapWithWildcard(usernameLike));
    }

    @Override
    public List<User> findByAddressLike(String addressLike) {
        return userRepository.findByAddressLike(wrapWithWildcard(addressLike));
    }

    @Override
    public List<User> findByFullNameLike(String fullNameLike) {
        return userRepository.findByFullNameLike(wrapWithWildcard(fullNameLike));
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public static String wrapWithWildcard(String str) {
        return WILDCARD + str + WILDCARD;
    }

}
