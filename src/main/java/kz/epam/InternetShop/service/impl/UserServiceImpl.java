package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.ValidationUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String WILDCARD = "%";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public static String wrapWithWildcard(String str) {
        return WILDCARD + str + WILDCARD;
    }
}
