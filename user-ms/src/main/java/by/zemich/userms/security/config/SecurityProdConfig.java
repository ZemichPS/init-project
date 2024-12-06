package by.zemich.userms.security.config;

import by.zemich.userms.dao.entities.User;
import by.zemich.userms.dao.repositories.UserRepository;
import by.zemich.userms.security.filters.CustomUsernamePasswordAuthenticationFilter;
import by.zemich.userms.security.filters.JWTFilter;
import by.zemich.userms.security.properties.JWTProperty;
import by.zemich.userms.security.utils.JWTHandler;
import by.zemich.userms.service.UserService;
import by.zemich.userms.service.exceptions.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityProdConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JWTFilter jwtFilter,
            AuthenticationManager authenticationManager,
            ObjectMapper mapper,
            JWTHandler jwtHandler
    ) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/users"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/users/{userId}/deactivation"),
                                antMatcher(HttpMethod.PATCH, "/api/v1/users/{userId}/activation")).hasRole("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/users/{userId}")).hasAnyRole()
                        .requestMatchers(antMatcher(HttpMethod.POST)).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/auth/login")).permitAll())
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(customUsernamePasswordAuthenticationFilter(authenticationManager, mapper, jwtHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository repository, PasswordEncoder encoder) {
        return email-> {
            User user = repository.findByEmail(email).orElseThrow(UserNotFoundException::new);
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(encoder.encode(user.getPassword()))
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService detailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        authenticationProvider.setUserDetailsService(detailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JWTFilter jwtFilter(JWTHandler jwtHandler, UserDetailsService userDetailsService) {
        return new JWTFilter(jwtHandler, userDetailsService);
    }

    @Bean
    public JWTHandler jwtHandler(JWTProperty jwtProperty) {
        return new JWTHandler(jwtProperty);
    }

    CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                                          ObjectMapper mapper,
                                                                                          JWTHandler jwtHandler
    ){
        return new CustomUsernamePasswordAuthenticationFilter(mapper, jwtHandler, authenticationManager);
    }
}
