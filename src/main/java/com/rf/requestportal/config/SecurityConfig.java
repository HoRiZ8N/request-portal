package com.rf.requestportal.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()

                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(antMatcher("/css/**")).permitAll()
                .requestMatchers(antMatcher("/login")).permitAll()
                .requestMatchers(antMatcher("/register")).permitAll()

                .requestMatchers(antMatcher(HttpMethod.GET,  "/requests/new")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.POST, "/requests")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.GET,  "/requests/my")).hasRole("USER")

                .requestMatchers(antMatcher(HttpMethod.GET,  "/requests")).hasRole("ADMIN")
                .requestMatchers(antMatcher(HttpMethod.GET,  "/requests/pending")).hasRole("ADMIN")
                .requestMatchers(antMatcher(HttpMethod.POST, "/requests/*/approve")).hasRole("ADMIN")
                .requestMatchers(antMatcher(HttpMethod.POST, "/requests/*/reject")).hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/h2-console/**")))
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
