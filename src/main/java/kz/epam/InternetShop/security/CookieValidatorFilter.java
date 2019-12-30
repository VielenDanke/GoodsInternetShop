package kz.epam.InternetShop.security;

import kz.epam.InternetShop.util.CookieUtil;
import kz.epam.InternetShop.util.exception.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_USER_TOKEN)) {
                    String tokenCookieValue = cookie.getValue();
                    UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByUserFromDbWithId(tokenCookieValue);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    CookieUtil.addCookie(response, AUTHORIZATION_USER_TOKEN, tokenProvider.createToken(authentication), AUTHORIZATION_COOKIE_EXPIRE_SECONDS);
                } else {
                    throw new NotFoundException("Please, login");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}
