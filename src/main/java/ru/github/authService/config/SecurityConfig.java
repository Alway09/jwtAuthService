package ru.github.authService.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.AuthClient;
import ru.github.authService.error.FilterChainExceptionHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final ClientRepository repository;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return login -> {
            log.debug("Authenticating '{}'", login);
            return new AuthClient(repository.getExisted(login));
        };
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger-ui/index.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtRequestFilter jwtRequestFilter,
                                           FilterChainExceptionHandler filterChainExceptionHandler)
            throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/api/*/auth/signin").permitAll()
                .requestMatchers("/api/*/client/signup").permitAll()
                .requestMatchers("/api/*/client/enable").permitAll()
                .requestMatchers("/api/*/auth/token").permitAll()
                .requestMatchers("/api/*/health").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
        return http.build();
    }
}
