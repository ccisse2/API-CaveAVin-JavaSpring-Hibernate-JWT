package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}
