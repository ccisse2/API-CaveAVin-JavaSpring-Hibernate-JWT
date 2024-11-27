package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Couleur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouleurRepository extends JpaRepository<Couleur, Integer> {
}
