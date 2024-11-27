package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    // Recherche d'un utilisateur par son pseudo'
    @Query("SELECT u FROM Utilisateur u WHERE u.pseudo = :pseudo")
    Utilisateur findByPseudo(@Param("pseudo") String pseudo);

    // Recherche d'un utilisateur par son pseudo et son mot de passe
    @Query("SELECT u FROM Utilisateur u WHERE u.pseudo = :pseudo AND u.password = :password")
    Utilisateur findByPseudoAndMotDePasse(@Param("pseudo") String pseudo, @Param("password") String password);
}
