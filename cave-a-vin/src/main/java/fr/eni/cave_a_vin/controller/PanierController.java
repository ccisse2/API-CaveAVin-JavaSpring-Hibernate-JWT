package fr.eni.cave_a_vin.controller;

import fr.eni.cave_a_vin.bll.PanierService;
import fr.eni.cave_a_vin.bo.Panier;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cette classe est un contrôleur REST permettant de gérer les paniers dans l'application.
 * Il fournit des points de terminaison pour récupérer, ajouter, mettre à jour et supprimer des paniers.
 * Le contrôleur est accessible aux utilisateurs ayant les rôles « Propriétaire » et « Client ».
 *
 * @auteur cisse
 */
@AllArgsConstructor
@RestController
@RequestMapping("/caveavin/paniers")
public class PanierController {

    private PanierService pService;

    /**
     * Récupère un panier par son identifiant unique.
     *
     * @param idInPath L'identifiant unique du panier sous forme de chaîne.
     * @return Une ResponseEntity contenant le panier s'il est trouvé, ou un code d'état HTTP et un message appropriés dans le cas contraire.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> rechercherPanierParId(@PathVariable("id") String idInPath) {
        // Toutes les données transmises par le protocole HTTP sont des chaines de
        // caractères par défaut
        // Il vaut mieux gérer les exceptions des données dans la méthode
        try {
            int id = Integer.parseInt(idInPath);
            final Panier emp = pService.chargerPanier(id);
            return ResponseEntity.ok(emp);
        } catch (NumberFormatException e) {
            // Statut 406 : No Acceptable
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        }
    }

    /**
     * Récupère une liste de paniers actifs pour un client spécifique.
     *
     * @param idClient L'identifiant unique du client sous forme de chaîne.
     * @return Une ResponseEntity contenant une liste des paniers actifs s'ils sont trouvés, ou un code d'état HTTP et un message appropriés dans le cas contraire.
     */
    @GetMapping("/client/actifs/{id}")
    public ResponseEntity<?> rechercherPaniersClientNonPayes(@PathVariable("id") String idClient) {
        final List<Panier> paniers = pService.chargerPaniersNonPayes(idClient);
        if (paniers == null || paniers.isEmpty()) {
            // Statut 204 : No Content - Pas de body car rien à afficher
            return ResponseEntity.noContent().build();
        }
        // Statut 200 : OK + dans le body paniers
        // Le contenu du body est directement injecté dans la méthode ok
        return ResponseEntity.ok(paniers);
    }

    /**
     * Récupère une liste des commandes terminées pour un client spécifique.
     *
     * @param idClient L'identifiant unique du client sous forme de chaîne.
     * @return Une ResponseEntity contenant une liste des commandes terminées si elles sont trouvées, ou un code d'état HTTP et un message appropriés dans le cas contraire.
     */
    @GetMapping("/client/commandes/{id}")
    public ResponseEntity<?> rechercherCommandesClient(@PathVariable("id") String idClient) {
        final List<Panier> paniers = pService.chargerCommandes(idClient);
        if (paniers == null || paniers.isEmpty()) {
            // Statut 204 : No Content - Pas de body car rien à afficher
            return ResponseEntity.noContent().build();
        }
        // Statut 200 : OK + dans le body paniers
        // Le contenu du body est directement injecté dans la méthode ok
        return ResponseEntity.ok(paniers);
    }

    /**
     * Ajoute un nouveau panier ou met à jour un panier existant.
     *
     * @param panier Le panier à ajouter ou à mettre à jour.
     * @return Une ResponseEntity contenant le panier mis à jour.
     */
    @PostMapping
    public ResponseEntity<?> ajouterPanier(@Valid @RequestBody Panier panier) {
        pService.ajouterOuMAJPanier(panier);
        return ResponseEntity.ok(panier);
    }

    /**
     * Marque un panier comme terminé (c'est-à-dire qu'un client a passé une commande).
     *
     * @param panier Le panier à marquer comme complété.
     * @return Une ResponseEntity contenant le panier mis à jour.
     */
    @PutMapping
    public ResponseEntity<?> passerCommande(@Valid @RequestBody Panier panier) {
        pService.passerCommande(panier);
        return ResponseEntity.ok(panier);
    }
}
