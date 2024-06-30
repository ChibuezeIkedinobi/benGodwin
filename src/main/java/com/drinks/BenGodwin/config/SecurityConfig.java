package com.drinks.BenGodwin.config;

import com.drinks.BenGodwin.CustomUserDetailsService;
import com.drinks.BenGodwin.repository.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UsersRepository usersRepository;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Hardcoded admin user
        User.UserBuilder users = User.builder().passwordEncoder(passwordEncoder::encode);
        org.springframework.security.core.userdetails.UserDetails admin = users
                .username("admin")
                .password("secureAdminPassword") // Replace "secureAdminPassword" with a real secure password!
                .roles("ADMIN")
                .build();

        // Custom UserDetailsService to fetch user from database and default to CASHIER role
        return new InMemoryUserDetailsManager(Arrays.asList(admin)) {
            @Override
            public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.equals("admin")) {
                    return admin;
                }
                com.drinks.BenGodwin.entity.Users appUser = usersRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

                return User.withUsername(appUser.getUsername())
                        .password(appUser.getPassword())
                        .authorities("ROLE_CASHIER")
                        .build();
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/cashier/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CASHIER")
                        .requestMatchers("/login", "/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
