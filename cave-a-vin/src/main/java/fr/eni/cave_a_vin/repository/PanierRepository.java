package fr.eni.cave_a_vin.repository;

import fr.eni.cave_a_vin.bo.Client;
import fr.eni.cave_a_vin.bo.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PanierRepository extends JpaRepository<Panier, Integer> {

    @Query("SELECT p FROM Panier p WHERE p.client = :client AND p.numCommande IS NULL")
    List<Panier> findPaniersByClientWithNullNumCommande(@Param("client") Client client);



    @Query(value = "SELECT * FROM CAV_SHOPPING_CART p WHERE p.CLIENT_ID = :clientId AND p.ORDER_NUMBER IS NOT NULL", nativeQuery = true)
    List<Panier> findCommandesByClientNative(@Param("clientId") String clientId);


    // Création d'une requête paramétrée avec JPQL
    @Query("SELECT p FROM Panier p WHERE p.client = :client AND p.numCommande = null")
    List<Panier> findPaniersWithJPQL(@Param("client") Client client);

    // Rechercher la liste des paniers non commandés d'un client
    List<Panier> findByNumCommandeNullAndClient(@Param("client") Client client);

    // Création d'une requête native - SQL
    @Query(value = "SELECT p.* FROM CAV_SHOPPING_CART p WHERE p.CLIENT_ID = :idClient AND p.ORDER_NUMBER IS NOT NULL", nativeQuery = true)
    List<Panier> findCommandesWithSQL(@Param("idClient") String idClient);

    // Rechercher la liste des commandes d'un client
    List<Panier> findByNumCommandeNotNullAndClient(@Param("client") Client client);
}
