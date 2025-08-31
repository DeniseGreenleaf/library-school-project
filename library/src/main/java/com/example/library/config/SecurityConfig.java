package com.example.library.config;

import com.example.library.User.CustomLogoutHandler;
import com.example.library.User.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //konfigurera vilka requests som kräver autentisering
                .authorizeHttpRequests(authz -> authz
                        //Tillåt alla requests till /api/public** utan autentisering
                          .requestMatchers("/api/public**").permitAll()
//
//                          // Läsning öppen för alla
//                          .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
//                          .requestMatchers(HttpMethod.GET, "/books/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/loans/**").permitAll()
//                                .requestMatchers(HttpMethod.GET, "/authors/**").permitAll()

                                // Böcker: alla kan läsa
                                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                                // men skapa/ändra/radera kräver roll
                                .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // registrering

                                // Användare: bara admin får se/hantera andra användare
                                .requestMatchers("/api/users/**").hasRole("ADMIN")

                                // Loans
                                .requestMatchers("/api/loans/**").hasRole("USER")

                                // Authors
                                .requestMatchers(HttpMethod.GET, "/api/authors/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/authors/**").hasRole("ADMIN")

                                .requestMatchers("/signup", "/register", "/public/**").permitAll()
//
//
//                        // Alla andra requests kräver autentisering
                         .anyRequest().authenticated()
                        )

                .formLogin(form -> form
                        .loginPage("/login")   // egen login-sida (om du vill)
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .userDetailsService(userDetailsService)
                // Aktivera HTTP Basic Authentication för API:er
                .httpBasic(Customizer.withDefaults())
                //Stäng av CSRF-skydd(behövs inte för API:er)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Använd vår UserDetailsService från databasen
        authProvider.setUserDetailsService(userDetailsService);

        //Använd Bcrypt för lsenordsjämförelse
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }



}
