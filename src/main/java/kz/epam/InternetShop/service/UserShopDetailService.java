package kz.epam.InternetShop.service;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserShopDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserShopDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userFromDb = userRepository.findByUsername(username);

        if (!userFromDb.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return userFromDb.get();
    }
}
