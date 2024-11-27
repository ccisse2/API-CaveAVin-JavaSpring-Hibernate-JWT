package fr.eni.cave_a_vin.bll;

import java.util.List;
import java.util.Optional;

import fr.eni.cave_a_vin.bo.Bouteille;
import fr.eni.cave_a_vin.bo.Couleur;
import fr.eni.cave_a_vin.bo.Region;
import fr.eni.cave_a_vin.repository.BouteilleRepository;
import fr.eni.cave_a_vin.repository.CouleurRepository;
import fr.eni.cave_a_vin.repository.RegionRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BouteilleServiceImpl implements BouteilleService {
	private BouteilleRepository bRepository;
	private RegionRepository rRepository;
	private CouleurRepository cRepository;

	@Override
	public List<Bouteille> chargerToutesBouteilles() {
		return bRepository.findAll();
	}

	@Override
	public Bouteille chargerBouteilleParId(int idBouteille) {
		// Validation de l'identifiant
		if (idBouteille <= 0) {
			throw new RuntimeException("Identifiant n'existe pas");
		}

		final Optional<Bouteille> opt = bRepository.findById(idBouteille);
		if (opt.isPresent()) {
			return opt.get();
		}
		// Identifiant correspond à aucun enregistrement en base
		throw new RuntimeException("Aucune bouteille ne correspond");
	}

	@Override
	public List<Bouteille> chargerBouteillesParRegion(int idRegion) {
		final Region rDB = validerRegion(idRegion);

		final List<Bouteille> listeDB = bRepository.findByRegion(rDB);
		if (listeDB == null || listeDB.isEmpty()) {
			throw new RuntimeException("Aucune bouteille ne correspond");
		}
		return listeDB;		
	}

	@Override
	public List<Bouteille> chargerBouteillesParCouleur(int idCouleur) {
		final Couleur cDB = validerCouleur(idCouleur);

		final List<Bouteille> listeDB = bRepository.findByCouleur(cDB);
		if (listeDB == null || listeDB.isEmpty()) {
			throw new RuntimeException("Aucune bouteille ne correspond");
		}
		return listeDB;		
	}

	@Override
	public void ajouter(Bouteille bouteille){
		// Validation des données
        try {
			validerRegion(bouteille.getRegion().getId());
			validerCouleur(bouteille.getCouleur().getId());
            bRepository.save(bouteille);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible d'inserer la bouteille ");
        }
    }

	@Override
	public void modifier(Bouteille bouteille) {
		//validation des données
		if (bouteille.getId() <= 0){
			throw new RuntimeException("L'identifiant de la bouteille est obligatoire");
		}
		Optional<Bouteille> bouteilleOpt = bRepository.findById(bouteille.getId());
		if (!bouteilleOpt.isPresent()) {
            throw new RuntimeException("Aucune bouteille ne correspond à l'identifiant fourni");
        }
		// Validation des nouvelles données
		try {
            ajouter(bouteille);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de modifier la bouteille");
        }
	}

	private Couleur validerCouleur(int idCouleur) {
		// Valider la Couleur
		if (idCouleur <= 0) {
			throw new RuntimeException("Identifiant n'existe pas");
		}

		final Optional<Couleur> opt = cRepository.findById(idCouleur);
		if (opt.isPresent()) {
			return opt.get();
		}
		// Identifiant correspond à aucun enregistrement en base
		throw new RuntimeException("Aucune couleur de vin ne correspond");
	}

	private Region validerRegion(int idRegion) {
		// Valider la Region
		if (idRegion <= 0) {
			throw new RuntimeException("Identifiant n'existe pas");
		}

		final Optional<Region> opt = rRepository.findById(idRegion);
		if (opt.isPresent()) {
			return opt.get();
		}
		// Identifiant correspond à aucun enregistrement en base
		throw new RuntimeException("Aucune région ne correspond");
	}
}
