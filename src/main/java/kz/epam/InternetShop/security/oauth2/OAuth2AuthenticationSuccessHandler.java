package kz.epam.InternetShop.security.oauth2;

import kz.epam.InternetShop.configuration.AppProperties;
import kz.epam.InternetShop.model.AuthProvider;
import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.security.TokenProvider;
import kz.epam.InternetShop.security.oauth2.user.OAuth2UserInfo;
import kz.epam.InternetShop.security.oauth2.user.OAuth2UserInfoFactory;
import kz.epam.InternetShop.util.CookieUtil;
import kz.epam.InternetShop.util.exception.BadRequestException;
import kz.epam.InternetShop.util.exception.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static kz.epam.InternetShop.util.ConstantUtil.*;

/**
 * This component of security handling successful OAuth2 authentication.
 * If user exists - updating user, if not - saving user as new one.
 *
 * Generate token for local validation and expiration for user in SecurityContext
 */

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final TokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        User user;
        Optional<User> userIfExist;
        DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOidcUser.getAttributes();

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = CookieUtil
                .getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);

        if (oAuth2AuthorizationRequest == null) {
            throw new OAuth2AuthenticationProcessingException("OAuth authorization request is null");
        }

        Map<String, Object> authorizationAttributes = oAuth2AuthorizationRequest.getAttributes();
        String provider = (String) authorizationAttributes.get(REGISTRATION_ID);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI " +
                    "and can't proceed with the authentication");
        }

        if (attributes.isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Cannot find user data");
        }

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, attributes);
        userIfExist = userRepository.findByUsername(oAuth2UserInfo.getEmail());

        user = userIfExist.map(oAuth2User -> updateExistingUser(oAuth2User, oAuth2UserInfo))
                .orElseGet(() -> registerNewUser(oAuth2UserInfo, provider));

        String targetUrl = redirectUri.orElse(LOCALHOST_MAIN_PAGE);
        String token = tokenProvider.createToken(user);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(TOKEN, token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }

    private User registerNewUser(OAuth2UserInfo oAuthUser, String provider) {
        User user = new User();

        user.setEnabled(1);
        user.setAuthority(Collections.singleton(Role.ROLE_USER));
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        user.setProviderId(oAuthUser.getId());
        user.setUsername(oAuthUser.getEmail());
        user.setFullName(oAuthUser.getName());
        user.setLocale(oAuthUser.getLocale());
        user.setGender(oAuthUser.getGender());
        user.setPassword(UUID.randomUUID().toString());
        user.setPicture(oAuthUser.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2User) {
        existingUser.setFullName(oAuth2User.getName());
        existingUser.setLocale(oAuth2User.getLocale());
        existingUser.setPicture(oAuth2User.getImageUrl());
        return userRepository.save(existingUser);
    }
}
