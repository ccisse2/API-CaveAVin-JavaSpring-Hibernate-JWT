package fr.eni.cave_a_vin.security.jwt;

import fr.eni.cave_a_vin.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Cette classe est responsable de la configuration de l'authentification JWT (JSON Web Tokens) dans l'application.
 * Il fournit des beans pour le service de détails utilisateur, le fournisseur d'authentification et le gestionnaire d'authentification.
 */
@Configuration
public class JwtAppConfig {
    /**
     * Instance Autowired de UtilisateurRepository pour récupérer les détails de l'utilisateur de la base de données.
     */
    @Autowired
    private UtilisateurRepository userRepository;

    /**
     * Méthode Bean pour créer une implémentation UserDetailsService.
     * Il récupère les détails de l'utilisateur de la base de données en utilisant le nom d'utilisateur fourni.
     * Si l'utilisateur n'est pas trouvé, il lève une exception UsernameNotFoundException.
     * <p>
     * Implémentation de @return UserDetailsService
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
    }


    @Bean
    /**
     * Méthode Bean pour créer une instance DaoAuthenticationProvider.
     * Il définit l'implémentation UserDetailsService créée précédemment.
     *
     * @return Instance DaoAuthenticationProvider
     */
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userDetailsService());
        return daoAuthProvider;
    }

    @Bean
    /**
     * Méthode pour créer une instance AuthenticationManager.
     * Il récupère l'AuthentificationManager par défaut à partir de l'AuthentificationConfiguration fournie.
     * <p>
     * Instance @param config AuthenticationConfiguration
     * Instance @return AuthenticationManager
     *
     * @throws Exception si une erreur survient lors de la récupération de l'AuthenticationManager
     */
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
