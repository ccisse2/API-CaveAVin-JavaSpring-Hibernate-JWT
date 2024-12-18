package fr.eni.cave_a_vin.bo;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = "CAV_OWNER")
public class Proprio extends Utilisateur{
    @Column(name = "CLIENT_NUMBER", length = 14)
    private String siret;
}
