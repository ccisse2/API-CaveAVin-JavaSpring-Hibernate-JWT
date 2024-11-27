package fr.eni.cave_a_vin.controller;


import fr.eni.cave_a_vin.bll.BouteilleService;
import fr.eni.cave_a_vin.bo.Bouteille;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur de gestion des bouteilles de vin dans une application cave.
 *
 * @auteur cisse
 */
@AllArgsConstructor
@RestController
@RequestMapping("/caveavin/bouteilles")
public class BouteilleController {

    private final BouteilleService bouteilleService;


    /**
     * Récupère toutes les bouteilles de vin de la base de données.
     *
     * @return ResponseEntity avec le statut 200 (OK) et une liste de bouteilles de vin dans le corps en cas de succès.
     * Statut 204 (Aucun contenu) si aucune bouteille de vin n'est trouvée.
     */
    @GetMapping
    public ResponseEntity<?> chargerToutesBouteilles() {
        List<Bouteille> bouteilles = bouteilleService.chargerToutesBouteilles();
        if (bouteilles == null || bouteilles.isEmpty()) {
            //status 204 : No content-pas de body car rien a afficher
            return ResponseEntity.noContent().build();
        }
        //statut 200 : ok + dans le body employes
        //le contenu du body est directement injecté dans la méthode ok
        return ResponseEntity.ok(bouteilles);
    }


    /**
     * Ajoute une nouvelle bouteille de vin à la base de données.
     *
     * @param bouteille La bouteille de vin à ajouter.
     * @return ResponseEntity avec le statut 201 (Créé) et la bouteille de vin ajoutée dans le corps en cas de succès.
     * Statut 500 (Erreur interne du serveur) si une erreur se produit.
     */
    @PostMapping
    public ResponseEntity<?> ajouterBouteille(@Valid @RequestBody Bouteille bouteille) {
        try {
            bouteilleService.ajouter(bouteille);
            //status 201 : created
            return ResponseEntity.ok(bouteille);
        } catch (RuntimeException e) {
            //status 500 : internal server error - erreur inattendue
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Erreur inattendue : " + e.getMessage());
        }
    }


    /**
     * Met à jour une bouteille de vin existante dans la base de données.
     *
     * @param bouteille La bouteille de vin mise à jour.
     * @return ResponseEntity avec le statut 200 (OK) et la bouteille de vin mise à jour dans le corps en cas de succès.
     * Statut 500 (Erreur interne du serveur) si une erreur se produit.
     */
    @PutMapping
    public ResponseEntity<?> modifierBouteille(@Valid @RequestBody Bouteille bouteille) {
        try {
            bouteilleService.modifier(bouteille);
            //status 200 : ok
            return ResponseEntity.ok(bouteille);
        } catch (RuntimeException e) {
            //status 500 : internal server error - erreur inattendue
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    /**
     * Récupère une bouteille de vin par son identifiant.
     *
     * @param id L'ID de la bouteille de vin à récupérer.
     * @return ResponseEntity avec le statut 200 (OK) et la bouteille de vin dans le corps en cas de succès.
     * Statut 404 (Not Found) si la bouteille de vin n'est pas trouvée.
     * Statut 406 (Non Acceptable) si l'ID n'est pas un entier valide.
     * Statut 500 (Erreur interne du serveur) si une erreur se produit.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> rechercherBouteilleParId(@PathVariable("id") String id) {
        try {
            int idBouteille = Integer.parseInt(id);
            Bouteille bouteille = bouteilleService.chargerBouteilleParId(idBouteille);
            if (bouteille == null) {
                //status 404 : not found
                return ResponseEntity.notFound().build();
            }
            //status 200 : ok + dans le body
            return ResponseEntity.ok(bouteille);

        } catch (NumberFormatException e) {
            //status 406
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("L'identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            //status 500 : internal server error - erreur inattendue
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue : " + e.getMessage());
        }
    }


    /**
     * Récupère les bouteilles de vin par région.
     *
     * @param id L'ID de la région.
     * @return ResponseEntity avec le statut 200 (OK) et une liste de bouteilles de vin dans le corps en cas de succès.
     * Statut 404 (Not Found) si aucune bouteille de vin n'est trouvée pour la région donnée.
     * Statut 406 (Non Acceptable) si l'ID n'est pas un entier valide.
     * Statut 500 (Erreur interne du serveur) si une erreur se produit.
     */
    @GetMapping("/region/{id}")
    public ResponseEntity<?> rechercherBouteilleParRegion(@PathVariable("id") String id) {
        try {
            int idRegion = Integer.parseInt(id);
            List<Bouteille> bouteilles = bouteilleService.chargerBouteillesParRegion(idRegion);
            if (bouteilles == null || bouteilles.isEmpty()) {
                //status 404 : not found
                return ResponseEntity.notFound().build();
            }
            //status 200 : ok + dans le body
            return ResponseEntity.ok(bouteilles);
        } catch (NumberFormatException e) {
            //status 406
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("L'identifiant n'est pas un entier");
        } catch (Exception e) {
            //status 500 : internal server error - erreur inattendue
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue : " + e.getMessage());
        }
    }


    /**
     * Récupère les bouteilles de vin par couleur.
     *
     * @param id L'ID de la couleur.
     * @return ResponseEntity avec le statut 200 (OK) et une liste de bouteilles de vin dans le corps en cas de succès.
     * Statut 404 (Introuvable) si aucune bouteille de vin n'est trouvée pour la couleur donnée.
     * Statut 406 (Non Acceptable) si l'ID n'est pas un entier valide.
     * Statut 500 (Erreur interne du serveur) si une erreur se produit.
     */
    @GetMapping("/couleur/{id}")
    public ResponseEntity<?> rechercherBouteilleParCouleur(@PathVariable("id") String id) {
        try {
            int idCouleur = Integer.parseInt(id);
            List<Bouteille> bouteilles = bouteilleService.chargerBouteillesParCouleur(idCouleur);
            if (bouteilles == null || bouteilles.isEmpty()) {
                //status 404 : not found
                return ResponseEntity.notFound().build();
            }
            //status 200 : ok + dans le body
            return ResponseEntity.ok(bouteilles);
        } catch (NumberFormatException e) {
            //status 406
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("L'identifiant n'est pas un entier");
        } catch (Exception e) {
            //status 500 : internal server error - erreur inattendue
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur inattendue : " + e.getMessage());
        }
    }
}
