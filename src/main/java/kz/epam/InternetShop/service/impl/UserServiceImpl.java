package kz.epam.InternetShop.service.impl;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String WILDCARD = "%";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @CacheEvict(value = "user")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    @Cacheable(value = "user")
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = NotFoundException.class, readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @CachePut(value = "user", key = "#user.username", unless = "#result == null")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User save(User user) {
        String userPasswordAfterEncoding = passwordEncoder.encode(user.getPassword());
        user.setPassword(userPasswordAfterEncoding);
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "user")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Cacheable(value = "user")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> findByUsernameLike(String usernameLike) {
        return userRepository.findByUsernameLike(wrapWithWildcard(usernameLike));
    }

    @Override
    @Cacheable(value = "user")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> findByAddressLike(String addressLike) {
        return userRepository.findByAddressLike(wrapWithWildcard(addressLike));
    }

    @Override
    @Cacheable(value = "user")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> findByFullNameLike(String fullNameLike) {
        return userRepository.findByFullNameLike(wrapWithWildcard(fullNameLike));
    }

    @Override
    @Cacheable(value = "user")
    @Transactional(propagation = Propagation.NEVER, readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @CacheEvict(value = "user")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public static String wrapWithWildcard(String str) {
        return WILDCARD + str + WILDCARD;
    }
}
