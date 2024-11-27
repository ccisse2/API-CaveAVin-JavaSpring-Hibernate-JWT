package fr.eni.cave_a_vin.bo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id"})
@Table(name = "CAV_LINE")
public class LignePanier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Integer id;

    @Column(name = "QUANTITY")
    private int quantite;

    @Column(name = "PRICE", nullable = false, precision = 2)
    private float prix;

    @ManyToOne
    @JoinColumn(name = "BOTTLE_ID")
    private Bouteille bouteille;
}
