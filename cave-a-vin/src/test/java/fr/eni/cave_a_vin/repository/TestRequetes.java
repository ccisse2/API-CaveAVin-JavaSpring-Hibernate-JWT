package fr.eni.cave_a_vin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import fr.eni.cave_a_vin.bo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
public class TestRequetes {
	private static final Logger log = LoggerFactory.getLogger(TestRequetes.class);
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Autowired
	BouteilleRepository bouteilleRepository;

	Region paysDeLaLoire;
	Couleur blanc;
	List<Bouteille> bouteilles;

	@BeforeEach
	void initDB() {
		jeuDeDonneesBouteilles();
		jeuDeDonneesUtilisateur();
	}

	private void jeuDeDonneesBouteilles() {
		final Couleur rouge = Couleur
				.builder()
				.nom("Rouge")
				.build();

		blanc = Couleur
				.builder()
				.nom("Blanc")
				.build();

		final Couleur rose = Couleur
				.builder()
				.nom("Rosé")
				.build();

		entityManager.persist(rouge);
		entityManager.persist(blanc);
		entityManager.persist(rose);
		entityManager.flush();

		final Region grandEst = Region
				.builder()
				.nom("Grand Est")
				.build();

		paysDeLaLoire = Region
				.builder()
				.nom("Pays de la Loire")
				.build();

		final Region nouvelleAquitaine = Region
				.builder()
				.nom("Nouvelle-Aquitaine")
				.build();

		entityManager.persist(grandEst);
		entityManager.persist(paysDeLaLoire);
		entityManager.persist(nouvelleAquitaine);
		entityManager.flush();

		bouteilles = new ArrayList<>();
		bouteilles.add(Bouteille
				.builder()
				.nom("Blanc du DOMAINE ENI Ecole")
				.millesime("2022")
				.prix(23.95f)
				.quantite(1298)
				.region(paysDeLaLoire)
				.couleur(blanc)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rouge du DOMAINE ENI Ecole")
				.millesime("2018")
				.prix(11.45f)
				.quantite(987)
				.region(paysDeLaLoire)
				.couleur(rouge)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Blanc du DOMAINE ENI Service")
				.millesime("2022")
				.prix(34)
				.petillant(true)
				.quantite(111)
				.region(grandEst)
				.couleur(blanc)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rouge du DOMAINE ENI Service")
				.millesime("2012")
				.prix(8.15f)
				.quantite(344)
				.region(paysDeLaLoire)
				.couleur(rouge)
				.build());
		bouteilles.add(Bouteille
				.builder()
				.nom("Rosé du DOMAINE ENI")
				.millesime("2020")
				.prix(33)
				.quantite(1987)
				.region(nouvelleAquitaine)
				.couleur(rose)
				.build());

		bouteilles.forEach(e -> {
			entityManager.persist(e);
			// Vérification de l'identifiant
			assertThat(e.getId()).isGreaterThan(0);
		});
		entityManager.flush();

	}

	private void jeuDeDonneesUtilisateur() {
		final List<Utilisateur> utilisateurs = new ArrayList<>();
		utilisateurs.add(Utilisateur
				.builder()
				.pseudo("harrisonford@email.fr")
				.password("IndianaJones3")
				.nom("Ford")
				.prenom("Harrison")
				.build());

		utilisateurs.add(Proprio
				.builder()
				.pseudo("georgelucas@email.fr")
				.password("Réalisateur&Producteur")
				.nom("Lucas")
				.prenom("George")
				.build());

		utilisateurs.add(Client
				.builder()
				.pseudo("natalieportman@email.fr")
				.password("MarsAttacks!")
				.nom("Portman")
				.prenom("Natalie")
				.build());

		// Contexte de la DB
		utilisateurs.forEach(e -> {
			entityManager.persist(e);
		});
		entityManager.flush();
	}

	//Test requette de la méthode findByRegion de BouteilleRepository
	@Test
	void testGetBouteillesParRegion() {
		Region region = Region.builder()
				.nom("Pays de la Loire")
				.build();
		final List<Bouteille> bouteillesDeLaLoire = bouteilleRepository.findByRegion(region); // Nom exact
		assertThat(bouteillesDeLaLoire).hasSize(3); // Il y a bien 3 bouteilles
		assertThat(bouteillesDeLaLoire).isNotEmpty();

		// Vérifiez les noms des bouteilles
		assertThat(bouteillesDeLaLoire)
				.extracting(Bouteille::getNom)
				.containsExactlyInAnyOrder(
						"Blanc du DOMAINE ENI Ecole",
						"Rouge du DOMAINE ENI Ecole",
						"Rouge du DOMAINE ENI Service"
				);

		// Log les bouteilles récupérées
		log.info("Bouteilles attendues : Blanc, Rouge du DOMAINE ENI Ecole, Rouge du DOMAINE ENI Service");
		log.info("Bouteilles retournées : {}", bouteillesDeLaLoire);
	}

	//Test de la méthode findByCouleur de bouteilleRepository
	@Test
    void testGetBouteillesParCouleur() {
		Couleur couleur = Couleur.builder()
				.nom("Blanc")
				.build();
		final List<Bouteille> bouteillesBlanches = bouteilleRepository.findByCouleur(couleur); // Nom exact
		assertThat(bouteillesBlanches).hasSize(2); // Il y a bien 2 bouteilles
		assertThat(bouteillesBlanches).isNotEmpty();
		// Vérifiez les noms des bouteilles
		assertThat(bouteillesBlanches)
                .extracting(Bouteille::getNom)
                .containsExactlyInAnyOrder(
                        "Blanc du DOMAINE ENI Ecole",
                        "Blanc du DOMAINE ENI Service"
                );

        // Log les bouteilles récupérées
        log.info("Bouteilles attendues : Blanc, Blanc du DOMAINE ENI Ecole, Blanc du DOMAINE ENI Service");
        log.info("Bouteilles retournées : {}", bouteillesBlanches);
	}

	//Test de la méthode findByPseudo de UtilisateurRepository
	@Test
    void testGetUtilisateurByPseudoAndMotDePasse() {
		final Utilisateur utilisateur = utilisateurRepository.findByPseudoAndMotDePasse("harrisonford@email.fr", "IndianaJones3");
		assertThat(utilisateur).isNotNull();
		assertThat(utilisateur.getNom()).isEqualTo("Ford");
		assertThat(utilisateur.getPrenom()).isEqualTo("Harrison");
		log.info("Utilisateur trouvé : {}", utilisateur);
	}

	//Test de la méthode findByPseudo de UtilisateurRepository
	@Test
    void testGetUtilisateurByPseudo() {
		final Utilisateur utilisateur = utilisateurRepository.findByPseudo("harrisonford@email.fr");
		assertThat(utilisateur).isNotNull();
		assertThat(utilisateur.getNom()).isEqualTo("Ford");
		assertThat(utilisateur.getPrenom()).isEqualTo("Harrison");
		log.info("Utilisateur trouvé : {}", utilisateur);
	}

}
