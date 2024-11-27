package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Proprio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprioRepository extends JpaRepository<Proprio, String> {
}
