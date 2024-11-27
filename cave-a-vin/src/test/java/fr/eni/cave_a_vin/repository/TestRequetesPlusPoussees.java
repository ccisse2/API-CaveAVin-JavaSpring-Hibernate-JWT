package fr.eni.cave_a_vin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import fr.eni.cave_a_vin.bo.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;


@DataJpaTest
public class TestRequetesPlusPoussees {
    private static final Logger log = LoggerFactory.getLogger(TestRequetesPlusPoussees.class);
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    BouteilleRepository bouteilleRepository;

    Region paysDeLaLoire;
    Couleur blanc;
    List<Bouteille> bouteilles;

    Client tom;

    @Autowired
    PanierRepository panierRepository;

    @BeforeEach
    void initDB() {
        jeuDeDonneesBouteilles();
        jeuDeDonneesPaniersClients();
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
                .nom("Nouvelle Aquitaine")
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

    private void jeuDeDonneesPaniersClients() {
        final Bouteille b1 = bouteilles.get(0);
        final Bouteille b2 = bouteilles.get(1);
        final Bouteille b3 = bouteilles.get(2);

        final List<Panier> paniers = new ArrayList<>();
        final Panier p1 = new Panier();
        final LignePanier lp1 = LignePanier
                .builder()
                .bouteille(b2)
                .quantite(3)
                .prix(3 * b2.getPrix())
                .build();
        p1.getLignesPanier().add(lp1);
        p1.setPrixTotal(lp1.getPrix());
        paniers.add(p1);

        final Panier p2 = new Panier();
        final LignePanier lp2 = LignePanier
                .builder()
                .bouteille(b1)
                .quantite(10)
                .prix(10 * b1.getPrix())
                .build();
        p2.getLignesPanier().add(lp2);
        p2.setPrixTotal(lp2.getPrix());
        paniers.add(p2);

        final Panier p3 = new Panier();
        final LignePanier lp3 = LignePanier
                .builder()
                .bouteille(b3)
                .quantite(4)
                .prix(3 * b3.getPrix())
                .build();
        p3.getLignesPanier().add(lp3);
        p3.setPrixTotal(lp3.getPrix());
        paniers.add(p3);

        tom = Client
                .builder()
                .pseudo("tomhanks@email.fr")
                .password("ForrestGump")
                .nom("Hanks")
                .prenom("Tom")
                .build();

        paniers.forEach(p -> {
            p.setClient(tom);
        });

        // Contexte de la DB
        paniers.forEach(p -> {
            entityManager.persist(p);
            assertThat(p.getId()).isGreaterThan(0);
        });
        entityManager.flush();

        // Indiquer les Panier de ce Client qui ont été commandés
        p1.setPaye(true);
        p1.setNumCommande(tom.getPseudo() + "_" + p1.getId());

        p3.setPaye(true);
        p3.setNumCommande(tom.getPseudo() + "_" + p3.getId());

        entityManager.merge(p1);
        entityManager.merge(p3);
        entityManager.flush();
    }

    @Test
    void test_utilisateurRepository_findPaniersWithJPQL() {
        final List<Panier> paniers = panierRepository.findPaniersByClientWithNullNumCommande(tom);
        // Vérification
        assertThat(paniers).isNotNull();
        assertThat(paniers).isNotEmpty();
        assertThat(paniers.size()).isEqualTo(1);

        Panier p = paniers.get(0);
        assertThat(p).isNotNull();
        assertThat(p.getNumCommande()).isNull();
    }

    @Test
    void test_utilisateurRepository_findByNumCommandeNullAndClient() {
        final List<Panier> paniers = panierRepository.findByNumCommandeNullAndClient(tom);
        assertThat(paniers).isNotNull();
        assertThat(paniers).isNotEmpty();
        assertThat(paniers.size()).isEqualTo(1);
        Panier p = paniers.get(0);
        assertThat(p).isNotNull();
        assertThat(p.getNumCommande()).isNull();
    }

    @Test
    void test_utilisateurRepository_findCommandesWithSQL() {
        final List<Panier> commandes = panierRepository.findCommandesByClientNative(tom.getPseudo());
        assertThat(commandes).isNotEmpty();
        assertThat(commandes.size()).isEqualTo(2);
        commandes.forEach(c -> {
            String numCommande = tom.getPseudo() + "_" + c.getId();
            assertThat(c.getNumCommande()).isNotNull();
            assertThat(c.getNumCommande()).isEqualTo(numCommande);
        });
    }

    @Test
    void test_utilisateurRepository_findByNumCommandeNotNullAndClient() {
        final List<Panier> commandes = panierRepository.findByNumCommandeNotNullAndClient(tom);
        assertThat(commandes).isNotNull();
        assertThat(commandes).isNotEmpty();
        assertThat(commandes.size()).isEqualTo(2);
        commandes.forEach(c -> {
            String numCommande = tom.getPseudo() + "_" + c.getId();
            assertThat(c.getNumCommande()).isNotNull();
            assertThat(c.getNumCommande()).isEqualTo(numCommande);
        });
    }
}
