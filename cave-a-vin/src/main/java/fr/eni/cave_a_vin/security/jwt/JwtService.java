package fr.eni.cave_a_vin.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Cette classe fournit des méthodes de gestion des JWT (JSON Web Tokens) pour l'authentification et l'autorisation.
 * Il utilise la bibliothèque io.jsonwebtoken pour la génération, l'analyse et la validation de jetons.
 *
 * @auteur VotreNom
 */
@Service
public class JwtService {
    /**
     * La clé secrète utilisée pour signer et vérifier les jetons JWT.
     * Cette valeur est chargée à partir du fichier de propriétés de l'application à l'aide de l'annotation @Value.
     */
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    /**
     * Renvoie la clé de signature pour la génération du jeton JWT.
     * La clé est obtenue en décodant le SECRET_KEY en base64.
     *
     * @return La clé de signature pour la génération de jetons JWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrait toutes les réclamations du jeton JWT donné.
     *
     * @param token Le jeton JWT à partir duquel extraire les revendications.
     * @return Les revendications extraites du jeton JWT.
     */
    private Claims extractAllClaims(String Token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(Token)
                .getBody();
    }

    /**
     * Extrait une revendication spécifique du jeton JWT donné à l'aide de la fonction de résolution de revendications fournie.
     *
     * @param token          Le jeton JWT à partir duquel extraire la réclamation.
     * @param claimsResolver La fonction permettant de résoudre la revendication à partir des revendications analysées.
     * @param <T>            Le type de réclamation.
     * @return La revendication extraite.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Extrait le nom d'utilisateur du jeton JWT donné.
     *
     * @param token Le jeton JWT à partir duquel extraire le nom d'utilisateur.
     * @return Le nom d'utilisateur extrait du jeton JWT.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Génère un nouveau jeton JWT avec les réclamations supplémentaires fournies et les détails de l'utilisateur.
     *
     * @param extraClaims Les revendications supplémentaires à inclure dans le jeton JWT.
     * @param userDetails Les détails de l'utilisateur à inclure dans le jeton JWT.
     * @return Le jeton JWT généré.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Génère un nouveau jeton JWT avec les détails de l'utilisateur fournis.
     * Cette méthode appelle la méthode generateToken avec une carte vide de revendications supplémentaires.
     *
     * @param userDetails Les détails de l'utilisateur à inclure dans le jeton JWT.
     * @return Le jeton JWT généré.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Vérifie si le jeton JWT donné est valide pour les détails de l'utilisateur fournis.
     *
     * @param token       Le jeton JWT à valider.
     * @param userDetails Les détails de l'utilisateur sur lesquels valider le jeton.
     * @return True si le jeton est valide pour les détails de l'utilisateur, false sinon.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Vérifie si le jeton JWT donné a expiré.
     *
     * @param token Le jeton JWT dont il faut vérifier l'expiration.
     * @return True si le token a expiré, false sinon.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du jeton JWT donné.
     *
     * @param token Le jeton JWT à partir duquel extraire la date d'expiration.
     * @return La date d'expiration extraite du jeton JWT.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
