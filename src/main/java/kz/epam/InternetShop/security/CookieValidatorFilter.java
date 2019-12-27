package kz.epam.InternetShop.security;

import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.interfaces.UserService;
import kz.epam.InternetShop.util.CookieUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kz.epam.InternetShop.util.ConstantUtil.AUTHORIZATION_COOKIE_EXPIRE_SECONDS;
import static kz.epam.InternetShop.util.ConstantUtil.AUTHORIZATION_USER_TOKEN;

public class CookieValidatorFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider;
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_USER_TOKEN)) {
                    String cookieValue = cookie.getValue();
                    Long userIdFromToken = tokenProvider.getUserIdFromToken(cookieValue);

                    User user = userRepository.findById(userIdFromToken).orElseThrow(() -> new NotFoundException("User not found"));

                    CookieUtil.addCookie(response, AUTHORIZATION_USER_TOKEN, tokenProvider.createToken(user), AUTHORIZATION_COOKIE_EXPIRE_SECONDS);
                } else {
                    throw new NotFoundException("Please, login");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}
