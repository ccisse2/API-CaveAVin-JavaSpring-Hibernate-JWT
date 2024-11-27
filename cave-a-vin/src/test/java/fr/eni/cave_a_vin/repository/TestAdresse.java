package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Adresse;
import fr.eni.cave_a_vin.bo.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;


@DataJpaTest
public class TestAdresse {

    private static final Logger log = LoggerFactory.getLogger(TestAdresse.class);


    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void test_save() {
        final Adresse adresse = Adresse.builder().rue("15 rue de Paris").codePostal("35000").ville("Rennes").build();
        final Client client = Client.builder().pseudo("bobeponge@email.fr").password("carré").nom("Eponge").prenom("Bob").build();
        client.setAdresse(adresse);
        // Appel du comportement
        final Client clientDB = clientRepository.save(client);
        log.info(clientDB.toString()); // Vérification de la cascade de l'association
        assertThat(clientDB.getAdresse()).isNotNull();
        assertThat(clientDB.getAdresse().getId()).isGreaterThan(0);
    }

    @Test
    public void test_delete() {
        final Adresse adresse = Adresse.builder().rue("15 rue de Paris").codePostal("35000").ville("Rennes").build();
        final Client client = Client.builder().pseudo("bobeponge@email.fr").password("carré").nom("Eponge").prenom("Bob").build(); // Association
        client.setAdresse(adresse); // Contexte de la DB
        entityManager.persist(client);
        entityManager.flush();
        assertThat(adresse.getId()).isGreaterThan(0); // Appel du comportement
        clientRepository.delete(client); // Vérification que l'entité a été supprimée
        Client clientDB = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB);
        Adresse adresseDB = entityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB);
    }


    @Test
    public void test_orphanRemoval() {
        final Adresse adresse = Adresse.builder().rue("15 rue de Paris").codePostal("35000").ville("Rennes").build();
        final Client client = Client.builder().pseudo("bobeponge@email.fr").password("carré").nom("Eponge").prenom("Bob").build(); // Association
        client.setAdresse(adresse);

        // Contexte de la DB
        entityManager.persist(client);
        entityManager.flush();
        assertThat(adresse.getId()).isGreaterThan(0); // Supprimer le lien entre l'entité Client et l'entité
        client.setAdresse(null); // Appel du comportement
        clientRepository.delete(client); // Vérification que l'entité a été supprimée
        Client clientDB = entityManager.find(Client.class, client.getPseudo());
        assertNull(clientDB);
        Adresse adresseDB = entityManager.find(Adresse.class, adresse.getId());
        assertNull(adresseDB);
    }
}