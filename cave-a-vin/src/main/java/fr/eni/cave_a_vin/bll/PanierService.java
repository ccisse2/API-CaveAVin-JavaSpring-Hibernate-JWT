package fr.eni.cave_a_vin.bll;

import java.util.List;

import fr.eni.cave_a_vin.bo.Panier;

public interface PanierService {

	Panier chargerPanier(int idPanier);
		
	List<Panier> chargerCommandes(String idclient);

	List<Panier> chargerPaniersNonPayes(String idclient);

	Panier ajouterOuMAJPanier(Panier p);
	
	Panier passerCommande(Panier panier);
}
