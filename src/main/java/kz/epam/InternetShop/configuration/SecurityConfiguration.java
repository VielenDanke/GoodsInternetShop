package kz.epam.InternetShop.configuration;

import kz.epam.InternetShop.repository.UserRepository;
import kz.epam.InternetShop.service.impl.AuthenticationProviderImpl;
import kz.epam.InternetShop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Bean("customUserDetailsService")
    public UserDetailsService customUserDetailsService() {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserRepository(userRepository);
        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("customAuthenticationProvider")
    public AuthenticationProvider customAuthenticationProvider() {
        AuthenticationProviderImpl authenticationProvider = new AuthenticationProviderImpl();
        authenticationProvider.setUserDetailsService(customUserDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}