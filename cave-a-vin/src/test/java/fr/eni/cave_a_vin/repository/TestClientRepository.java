package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Client;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TestClientRepository {

    private static final Logger log = LoggerFactory.getLogger(TestClientRepository.class);

    @Autowired
    private ClientRepository clientRepository;

    // Test pour sauvegarder un client
    @Test
    public void testSaveClient() {
        // Création d'un client
        Client client = Client.builder()
                .pseudo("bobeponge@email.fr")
                .nom("Eponge")
                .prenom("Bob")
                .password("12345")
                .build();

        // Sauvegarde du client
        Client clientSaved = clientRepository.save(client);
        log.info("Client sauvegardé : {}", clientSaved);

        // Vérification que le client est bien sauvegardé
        assertThat(clientSaved).isNotNull();
        assertThat(clientSaved.getPseudo()).isEqualTo("bobeponge@email.fr");
        assertThat(clientRepository.findById(clientSaved.getPseudo()).isPresent()).isTrue();
    }

    // Test pour supprimer un client
    @Test
    public void testDeleteClient() {
        // Création d'un client
        Client client = Client.builder()
                .pseudo("bobeponge@email.fr")
                .nom("Eponge")
                .prenom("Bob")
                .password("12345")
                .build();

        // Sauvegarde du client
        clientRepository.save(client);

        // Suppression du client
        clientRepository.deleteById(client.getPseudo());

        // Vérification que le client n'existe plus
        assertThat(clientRepository.findById(client.getPseudo()).isPresent()).isFalse();
    }

    // Test pour vérifier que le mot de passe n'est pas tracé par toString
    @Test
    public void testToStringClient() {
        // Création d'un client
        Client client = Client.builder()
                .pseudo("bobeponge@email.fr")
                .nom("Eponge")
                .prenom("Bob")
                .password("12345")
                .build();

        // Vérification que le mot de passe n'est pas inclus dans toString
        String clientString = client.toString();
        log.info("Client.toString() : {}", clientString);

        assertThat(clientString).doesNotContain("12345"); // Mot de passe brut
        assertThat(clientString).doesNotContain("password"); // Clé 'password'
    }
}
