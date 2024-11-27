package fr.eni.cave_a_vin.security.jwt;

import fr.eni.cave_a_vin.bo.Utilisateur;
import fr.eni.cave_a_vin.repository.UtilisateurRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private UtilisateurRepository userInfoRepository;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    /**
     * Authentifie un utilisateur à l'aide du {@link AuthenticationRequest} fourni et génère un jeton JWT.
     *
     * @param request Le {@link AuthenticationRequest} contenant les informations d'identification de l'utilisateur.
     * @return Une {@link AuthenticationResponse} contenant le jeton JWT généré.
     * @throws AuthenticationException Si les informations d'identification fournies ne sont pas valides.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPseudo(), request.getPassword()));

        } catch (AuthenticationException e) {

            throw new RuntimeException(e);
        }
        Utilisateur user = userInfoRepository.findById(request.getPseudo()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken(jwtToken);
        return authResponse;
    }
}