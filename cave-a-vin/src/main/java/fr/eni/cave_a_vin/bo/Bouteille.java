package fr.eni.cave_a_vin.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "CAV_BOTTLE")
public class Bouteille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOTTLE_ID")
    private Integer id;

    @Column(name = "NAME", length = 255, unique = true, nullable = false)
    @NotBlank(message = "{bouteille.nom.not-blank}")
    @Size(max = 255, message = "{bouteille.nom.size-error}")
    private String nom;

    @Column(name = "SPARKLING")
    private boolean petillant;

    @Column(name = "VINTAGE", length = 100)
    @Size(max = 100, message = "{bouteille.millesime.size-error}")
    private String millesime;

    @Column(name = "QUANTITY")
    @Min(value = 1, message = "{bouteille.quantite.min-error}")
    private int quantite;

    @Column(name = "PRICE", precision = 2)
    @Min(value = 1, message = "{bouteille.prix.min-error}")
    private float prix;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "REGION_ID")
    @NotNull(message = "{bouteille.region.not-null}")
    private Region region;


    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "COLOR_ID")
    @NotNull(message = "{bouteille.couleur.not-null}")
    private Couleur couleur;

}
