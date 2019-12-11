package kz.epam.InternetShop.configuration;

import kz.epam.InternetShop.model.Role;
import kz.epam.InternetShop.model.User;
import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.impl.AuthenticationProviderImpl;
import kz.epam.InternetShop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String GOOGLE_LOGIN_URL = "/login/google";
    private static final String VKONTAKTE_LOGIN_URL = "/login/vk";

    private UserRepository userRepository;
    private OAuth2ClientContext oAuth2ClientContext;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/registration**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();

        http.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("googlePrincipalExtractor")
    public PrincipalExtractor googlePrincipalExtractor() {
        return map -> {
            String username = (String) map.get("email");

            User user = userRepository.findByUsername(username).orElseGet(() -> {
                User newUser = new User();

                newUser.setFullName((String) map.get("name"));
                newUser.setUsername(username);
                newUser.setGender((String) map.get("gender"));
                newUser.setLocale((String) map.get("locale"));
                newUser.setPassword(passwordEncoder().encode("oauth2client"));
                newUser.setAuthority(Collections.singleton(Role.ROLE_USER));
                newUser.setEnabled(1);

                return newUser;
            });
            return userRepository.save(user);
        };
    }

    @Bean("vKontaktePrincipalExtractor")
    public PrincipalExtractor vKontaktePrincipalExtractor() {
        return map -> {
            String username = (String) map.get("screen_name");

            User user = userRepository.findByUsername(username).orElseGet(() -> {
                User newUser = new User();
                String sex = "";

                switch ((Integer) map.get("sex")) {
                    case 0:
                        sex = "";
                        break;
                    case 1:
                        sex = "woman";
                        break;
                    case 2:
                        sex = "man";
                        break;
                    default:
                        sex = "empty";
                        break;
                }

                newUser.setFullName((String) map.get("first_name") + map.get("last_name"));
                newUser.setUsername(username);
                newUser.setGender(sex);
                newUser.setLocale((String) map.get("country.title"));
                newUser.setPassword(passwordEncoder().encode("oauth2client"));
                newUser.setAuthority(Collections.singleton(Role.ROLE_USER));
                newUser.setEnabled(1);

                return newUser;
            });
            return userRepository.save(user);
        };
    }

    @Bean("customUserDetailsService")
    public UserDetailsService customUserDetailsService() {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserRepository(userRepository);
        return userDetailsService;
    }

    @Bean("customAuthenticationProvider")
    public AuthenticationProvider customAuthenticationProvider() {
        AuthenticationProviderImpl authenticationProvider = new AuthenticationProviderImpl();
        authenticationProvider.setUserDetailsService(customUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(oAuth2ClientContextFilter);
        filterRegistrationBean.setOrder(-100);
        return filterRegistrationBean;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("vkontakte.client")
    public AuthorizationCodeResourceDetails vKontakte() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("vkontakte.resource")
    public ResourceServerProperties vKontakteResource() {
        return new ResourceServerProperties();
    }

    private Filter ssoFilter() {

        CompositeFilter filter = new CompositeFilter();
        List<Filter> filterList = new ArrayList<>();

        filterList.add(authenticationByExistProvider(google(), googleResource(), GOOGLE_LOGIN_URL, googlePrincipalExtractor()));
        filterList.add(authenticationByExistProvider(vKontakte(), vKontakteResource(), VKONTAKTE_LOGIN_URL, vKontaktePrincipalExtractor()));

        filter.setFilters(filterList);

        return filter;
    }

    private Filter authenticationByExistProvider(AuthorizationCodeResourceDetails resourceDetails,
                                                 ResourceServerProperties serverProperties,
                                                 String loginUrl, PrincipalExtractor principalExtractor) {

        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter(loginUrl);
        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, oAuth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices =
                new UserInfoTokenServices(serverProperties.getUserInfoUri(), resourceDetails.getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setPrincipalExtractor(principalExtractor);
        filter.setTokenServices(tokenServices);

        return filter;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setoAuth2ClientContext(@Qualifier("oauth2ClientContext") OAuth2ClientContext oAuth2ClientContext) {
        this.oAuth2ClientContext = oAuth2ClientContext;
    }
}