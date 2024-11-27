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
@Table(name = "CAV_REGION")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REGION_ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String nom;
}
