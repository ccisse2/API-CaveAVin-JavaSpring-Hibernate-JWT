package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Bouteille;
import fr.eni.cave_a_vin.bo.Couleur;
import fr.eni.cave_a_vin.bo.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BouteilleRepository extends JpaRepository<Bouteille, Integer> {
    // Rechercher des bouteilles par leur région
    List<Bouteille> findByRegion(@Param("r") Region r);

    // Rechercher des bouteilles par leur couleur
    List<Bouteille> findByCouleur(@Param("c") Couleur c);

    // Rechercher une bouteille par son nom
    Bouteille findByNom(@Param("nom") String nom);
}
