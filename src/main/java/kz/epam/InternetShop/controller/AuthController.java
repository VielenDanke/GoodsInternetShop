package kz.epam.InternetShop.controller;

import kz.epam.InternetShop.model.AuthProvider;
import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.payload.ApiResponse;
import kz.epam.InternetShop.payload.AuthResponse;
import kz.epam.InternetShop.payload.LoginRequest;
import kz.epam.InternetShop.payload.SignUpRequest;
import kz.epam.InternetShop.security.TokenProvider;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static kz.epam.InternetShop.util.ConstantUtil.AUTHORIZATION_COOKIE_EXPIRE_SECONDS;
import static kz.epam.InternetShop.util.ConstantUtil.AUTHORIZATION_USER_TOKEN;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        // Saving token in cookie for validation in @CookieValidatorFilter
        CookieUtil.addCookie(response, AUTHORIZATION_USER_TOKEN, token, AUTHORIZATION_COOKIE_EXPIRE_SECONDS);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        Optional<User> userFromDB = userService.findByUsername(signUpRequest.getUsername());

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setFullName(signUpRequest.getFullName());
        user.setAddress(signUpRequest.getAddress());
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(1);
        user.setAuthority(Collections.singleton(Role.ROLE_USER));

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/shop/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }
}
