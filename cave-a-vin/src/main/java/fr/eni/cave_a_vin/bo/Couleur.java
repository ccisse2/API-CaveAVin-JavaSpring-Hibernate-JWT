package fr.eni.cave_a_vin.bo;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "CAV_COLOR")
public class Couleur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLOR_ID")
    private Integer id;

    @Column(name = "COLOR_NAME", length = 250, nullable = false, unique = true)
    private String nom;
}
