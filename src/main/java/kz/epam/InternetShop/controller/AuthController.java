package kz.epam.InternetShop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kz.epam.InternetShop.model.AuthProvider;
import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.payload.ApiResponse;
import kz.epam.InternetShop.payload.AuthResponse;
import kz.epam.InternetShop.payload.LoginRequest;
import kz.epam.InternetShop.payload.SignUpRequest;
import kz.epam.InternetShop.security.TokenProvider;
import kz.epam.InternetShop.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(value = "Authorization and registration management")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @ApiOperation(value = "Login with Details Service", response = ResponseEntity.class, httpMethod = "POST")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @ApiOperation(value = "Registration with local service", response = ResponseEntity.class, httpMethod = "POST")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        Optional<User> userFromDB = userService.findByUsername(signUpRequest.getEmail());

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setUsername(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setFullName(signUpRequest.getName());
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
