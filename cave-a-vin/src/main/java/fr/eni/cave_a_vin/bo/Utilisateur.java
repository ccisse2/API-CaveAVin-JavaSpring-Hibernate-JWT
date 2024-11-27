package fr.eni.cave_a_vin.bo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"pseudo"})
@Table(name = "CAV_USER")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur implements UserDetails {
    @Id
    @Column(name = "LOGIN", nullable = false, length = 255, unique = true)
    private String pseudo;

    @Column(name = "LAST_NAME",nullable = false, length = 60)
    private String nom;

    @Column(name = "FIRST_NAME", nullable = false, length = 150)
    private String prenom;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    @ToString.Exclude // Exclut le mot de passe du toString généré par Lombok
    private String password;

    @Column(name = "AUTHORITY", length = 15)
    @ToString.Exclude
    private String authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getUsername() {
        return pseudo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
