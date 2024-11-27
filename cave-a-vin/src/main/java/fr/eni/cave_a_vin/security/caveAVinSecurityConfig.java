package fr.eni.cave_a_vin.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class caveAVinSecurityConfig {

    @Autowired
    private Filter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui.html").permitAll()
                    .requestMatchers("/caveavin/bouteilles").permitAll()  // Accessible à tous
                    .requestMatchers("/caveavin/auth").permitAll()
                    .requestMatchers("/caveavin/bouteilles/**").hasAnyRole("CLIENT", "OWNER") // Accessible aux Clients et Propriétaires
                    .requestMatchers("/caveavin/paniers/**").hasRole("CLIENT")               // Accessible uniquement aux Clients
                    .requestMatchers("/caveavin/paniers/client/actifs/**").hasRole("OWNER") // Accessible aux Propriétaires
                    .anyRequest().authenticated();
        });
        http.csrf().disable();

        //Connexion de l'utilisateur
        http.authenticationProvider(authenticationProvider);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        //Session Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Ajout du filtre JWT
        http.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
