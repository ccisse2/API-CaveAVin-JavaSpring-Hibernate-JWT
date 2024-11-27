package fr.eni.cave_a_vin.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.eni.cave_a_vin.bo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;


@DataJpaTest
public class TestManyToOnePanierClient {
    private static final Logger log = LoggerFactory.getLogger(TestManyToOnePanierClient.class);
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    PanierRepository panierRepository;

    @Autowired
    BouteilleRepository bouteilleRepository;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    public void initDB() {
        final List<Couleur> couleurs = new ArrayList<>();
        couleurs.add(Couleur
                .builder()
                .nom("Blanc")
                .build());
        couleurs.add(Couleur
                .builder()
                .nom("Rouge")
                .build());

        couleurs.forEach(item -> {
            entityManager.persist(item);
            assertThat(item.getId()).isGreaterThan(0);
        });
        entityManager.flush();

        final List<Region> regions = new ArrayList<>();
        regions.add(Region
                .builder()
                .nom("Pays de la Loire")
                .build());

        regions.add(Region
                .builder()
                .nom("Grand Est")
                .build());

        regions.forEach(item -> {
            entityManager.persist(item);
            assertThat(item.getId()).isGreaterThan(0);
        });
        entityManager.flush();

        final List<Bouteille> bouteilles = new ArrayList<>();
        bouteilles.add(Bouteille
                .builder()
                .nom("DOMAINE ENI Ecole")
                .millesime("2022")
                .prix(11.45f)
                .quantite(1298)
                .region(regions.get(0))
                .couleur(couleurs.get(0))
                .build());

        bouteilles.add(Bouteille
                .builder()
                .nom("DOMAINE ENI Service")
                .millesime("2015")
                .prix(23.95f)
                .quantite(2998)
                .region(regions.get(1))
                .couleur(couleurs.get(1))
                .build());

        bouteilles.forEach(item -> {
            entityManager.persist(item);
            assertThat(item.getId()).isGreaterThan(0);
        });
        entityManager.flush();
    }

    private List<Panier> jeuDeDonnees() {
        final List<Bouteille> bouteilles = bouteilleRepository.findAll();
        assertThat(bouteilles).isNotNull();
        assertThat(bouteilles).isNotEmpty();
        assertThat(bouteilles.size()).isEqualTo(2);

        final List<Panier> paniers = new ArrayList<>();

        final Panier p1 = new Panier();
        int qte1 = 3;
        final Bouteille b1 = bouteilles.get(0);
        final LignePanier lp1 = LignePanier
                .builder()
                .bouteille(b1)
                .quantite(qte1)
                .prix(qte1 * b1.getPrix())
                .build();
        p1.getLignesPanier().add(lp1);
        p1.setPrixTotal(lp1.getPrix());
        paniers.add(p1);

        final Panier p2 = new Panier();
        int qte2 = 10;
        final Bouteille b2 = bouteilles.get(1);
        final LignePanier lp2 = LignePanier
                .builder()
                .bouteille(b2)
                .quantite(qte2)
                .prix(qte2 * b2.getPrix())
                .build();
        p2.getLignesPanier().add(lp2);
        p2.setPrixTotal(lp2.getPrix());
        paniers.add(p2);

        return paniers;
    }

    //test_save_1panier sauvegarde un Panier et son Client associé
    @Test
    public void test_save_1panier() {
        // Initialisation des données
        final List<Panier> paniers = jeuDeDonnees();
        final Panier panier = paniers.get(0);

        // Création du client associé
        final Client client = Client.builder()
                .pseudo("john.doe@email.com")
                .password("securepassword")
                .nom("Doe")
                .prenom("John")
                .build();

        // Associer le client au panier
        panier.setClient(client);

        // Persist le client et le panier
        entityManager.persist(client);
        assertThat(client.getPseudo()).isNotNull();

        entityManager.persist(panier);
        assertThat(panier.getId()).isGreaterThan(0);

        // Vérification des données persistées
        final Panier savedPanier = panierRepository.findById(panier.getId()).orElse(null);
        assertThat(savedPanier).isNotNull();
        assertThat(savedPanier.getClient()).isNotNull();
        assertThat(savedPanier.getClient().getPseudo()).isEqualTo("john.doe@email.com");
        assertThat(savedPanier.getLignesPanier()).hasSize(1);
        assertThat(savedPanier.getPrixTotal()).isEqualTo(panier.getPrixTotal());

        // Vérification de la relation avec LignePanier
        assertThat(savedPanier.getLignesPanier().get(0).getBouteille().getNom()).isEqualTo("DOMAINE ENI Ecole");

        log.info("Panier sauvegardé avec succès : {}", savedPanier);
    }

    //test_save_paniers_unClient → sauvegarder plusieurs Panier d’un même Client

    @Test
    public void test_save_paniers_unClient() {
        // Initialisation des données
        final List<Panier> paniers = jeuDeDonnees();

        // Création du client associé
        final Client client = Client.builder()
                .pseudo("john.doe@email.com")
                .password("securepassword")
                .nom("Doe")
                .prenom("John")
                .build();

        // Associer le client aux paniers
        paniers.forEach(panier -> panier.setClient(client));

        // Persist le client et les paniers
        entityManager.persist(client);
        assertThat(client.getPseudo()).isNotNull();

        paniers.forEach(panier -> {
            entityManager.persist(panier);
            assertThat(panier.getId()).isGreaterThan(0);
        });

        // Récupérer les paniers sauvegardés depuis la base
        List<Panier> savedPaniers = panierRepository.findAll();
        assertThat(savedPaniers).isNotNull();
        assertThat(savedPaniers).hasSize(2);

        // Vérification des données persistées
        assertThat(savedPaniers.get(0).getClient().getPseudo()).isEqualTo("john.doe@email.com");
        assertThat(savedPaniers.get(1).getClient().getPseudo()).isEqualTo("john.doe@email.com");

        // Vérification de la relation avec LignePanier
        assertThat(savedPaniers.get(0).getLignesPanier().get(0).getBouteille().getNom()).isEqualTo("DOMAINE ENI Ecole");
        assertThat(savedPaniers.get(1).getLignesPanier().get(0).getBouteille().getNom()).isEqualTo("DOMAINE ENI Service");

        // Logging des résultats pour débogage
        log.info("Paniers sauvegardés : {}", savedPaniers);
    }

    //test_delete → montrer que supprimer le Client ne supprime pas les Panier
    @Test
    public void test_delete() {
        // Initialisation des données
        final List<Panier> paniers = jeuDeDonnees();

        // Création du client associé
        final Client client = Client.builder()
                .pseudo("john.doe@email.com")
                .password("securepassword")
                .nom("Doe")
                .prenom("John")
                .build();

        // Associer le client aux paniers
        paniers.forEach(panier -> panier.setClient(client));

        // Persist le client et les paniers
        entityManager.persist(client);
        assertThat(client.getPseudo()).isNotNull();

        paniers.forEach(panier -> {
            entityManager.persist(panier);
            assertThat(panier.getId()).isGreaterThan(0);
        });
        entityManager.flush();

        List<Integer> listeIdPanier = paniers.stream().map(Panier::getId).collect(Collectors.toList());
        assertThat(listeIdPanier).isNotNull();
        assertThat(listeIdPanier).isNotEmpty();
        assertThat(listeIdPanier.size()).isEqualTo(2);

        // Supprimer le client
        clientRepository.delete(client);

        final Client deletedClient = clientRepository.findById(client.getPseudo()).orElse(null);
        assertThat(deletedClient).isNull();
        log.info("Client supprimé avec succès : {}", client);

        // Vérifier que les paniers ne sont pas supprimés

        assertThat(listeIdPanier).isNotNull();
        assertThat(listeIdPanier).isNotEmpty();
        listeIdPanier.forEach(id -> {
            assertThat(id).isGreaterThan(0);
            Panier panierDB = entityManager.find(Panier.class, id);
            assertNotNull(panierDB);
        });
        log.info("Paniers restants : {}", listeIdPanier);
    }


}
