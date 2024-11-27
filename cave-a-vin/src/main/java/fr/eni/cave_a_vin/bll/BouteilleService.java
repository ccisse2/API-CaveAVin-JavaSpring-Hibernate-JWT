package fr.eni.cave_a_vin.bll;

import java.util.List;

import fr.eni.cave_a_vin.bo.Bouteille;

public interface BouteilleService {
	List<Bouteille> chargerToutesBouteilles();
	
	Bouteille chargerBouteilleParId(int idBouteille);

	List<Bouteille> chargerBouteillesParRegion(int idRegion);

	List<Bouteille> chargerBouteillesParCouleur(int idCouleur);

	void ajouter(Bouteille bouteille);

	void modifier(Bouteille bouteille);
}
