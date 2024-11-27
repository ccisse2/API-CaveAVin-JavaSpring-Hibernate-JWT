package fr.eni.cave_a_vin.bo;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"rue", "codePostal"})
@Entity
@Table(name = "CAV_ADDRESS")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Integer id;

    @Column(name = "STREET", nullable = false, length = 250)
    private String rue;

    @Column(name = "POSTAL_CODE", nullable = false, length = 5)
    private String codePostal;

    @Column(name = "CITY", nullable = false, length = 150)
    private String ville;

}
