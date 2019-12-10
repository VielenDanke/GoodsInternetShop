package kz.epam.InternetShop.configuration;

import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.impl.AuthenticationProviderImpl;
import kz.epam.InternetShop.service.impl.ResourceServerTokenServicesImpl;
import kz.epam.InternetShop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
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
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;
    private OAuth2ClientContext oAuth2ClientContext;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login/**", "/registration**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .and()
                .logout().permitAll();

        http.addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

    private Filter ssoFilter() {

        CompositeFilter filter = new CompositeFilter();
        List<Filter> filterList = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter googleFilter =
                new OAuth2ClientAuthenticationProcessingFilter("/login/google");
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        googleFilter.setRestTemplate(googleTemplate);
        ResourceServerTokenServicesImpl googleTokenService =
                new ResourceServerTokenServicesImpl(googleResource().getUserInfoUri(), google().getClientId());
        googleTokenService.setRestTemplate(googleTemplate);
        googleFilter.setTokenServices(googleTokenService);
        googleTokenService.setUserRepository(userRepository);
        googleTokenService.setPasswordEncoder(passwordEncoder());

        filterList.add(googleFilter);

        filter.setFilters(filterList);

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