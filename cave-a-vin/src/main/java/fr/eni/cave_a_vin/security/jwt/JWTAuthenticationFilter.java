package fr.eni.cave_a_vin.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Cette classe est un filtre personnalisé pour Spring Security qui gère l'authentification JWT.
 * Il étend {@link OncePerRequestFilter} et remplace la méthode {@link #doFilterInternal} pour effectuer le processus d'authentification JWT.
 */
@Component
@AllArgsConstructor
@Primary
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Le service responsable de la gestion des opérations JWT.
     */
    private JwtService jwtService;

    /**
     * Le service chargé de récupérer les détails de l'utilisateur.
     */
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extrait le JWT de l'en-tête de la requête
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si aucun JWT n'est présent, continuez la chaîne de filtres
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        // Extraire l'e-mail OU LE PSEUDO de l'utilisateur du JWT
        final String userPseudo = jwtService.extractUserName(jwt);

        // Validation des données utilisateur par rapport à la base de données
        if (userPseudo != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Archiver la base de données
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userPseudo);

            // Valide le JWT
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Gère le contexte de sécurité de l'utilisateur
                // Crée un nouveau jeton d'authentification avec les informations et les rôles de l'utilisateur
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userPseudo,
                        null, userDetails.getAuthorities());
                // Définit les détails de la demande d'origine
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Mettre à jour le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
