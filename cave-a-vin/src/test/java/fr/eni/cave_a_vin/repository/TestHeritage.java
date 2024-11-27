package fr.eni.cave_a_vin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import fr.eni.cave_a_vin.bo.Client;
import fr.eni.cave_a_vin.bo.Proprio;
import fr.eni.cave_a_vin.bo.Utilisateur;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;



@DataJpaTest
public class TestHeritage {
	private static final Logger log = LoggerFactory.getLogger(TestHeritage.class);
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Autowired
	ProprioRepository proprioRepository;

	@Autowired
	ClientRepository clientRepository;

	@BeforeEach
	public void initDB() {
		List<Utilisateur> utilisateurs = new ArrayList<>();
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
				.siret("12345678901234")
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
	}

	//Test findAllUtilisateur dois retourner les entité creer ci dessus
	@Test
	public void test_findAll_utilisateur(){
		List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(3);
		assertThat(utilisateurs).isNotNull();
		assertThat(utilisateurs).isNotEmpty();
        assertThat(utilisateurs.get(0).getPseudo()).isEqualTo("harrisonford@email.fr");
        assertThat(utilisateurs.get(1).getPseudo()).isEqualTo("georgelucas@email.fr");
        assertThat(utilisateurs.get(2).getPseudo()).isEqualTo("natalieportman@email.fr");
		log.info("Les utilisateurs ont bien été récupérés."+ utilisateurs.toString());
	}

	//Test findAllProprio dois retourner les proprios creer ci dessus
	@Test
    public void test_findAll_proprio(){
		final List<Proprio> proprios = proprioRepository.findAll();

		assertThat(proprios).hasSize(1);
		assertThat(proprios).isNotNull();
		assertThat(proprios).isNotEmpty();
		assertThat(proprios.get(0).getPseudo()).isEqualTo("georgelucas@email.fr");
		log.info("Les proprios ont bien été récupérés."+ proprios.toString());
	}


	//Test findAllClient dois retourner les clients creer ci dessus
	@Test
	public void test_findAll_client(){
		final List<Client> clients = clientRepository.findAll();

        assertThat(clients).hasSize(1);
        assertThat(clients).isNotNull();
        assertThat(clients).isNotEmpty();
        assertThat(clients.get(0).getPseudo()).isEqualTo("natalieportman@email.fr");
        log.info("Les clients ont bien été récupérés."+ clients.toString());
	}
}
