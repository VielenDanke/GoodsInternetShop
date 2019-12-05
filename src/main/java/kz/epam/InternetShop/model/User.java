package kz.epam.InternetShop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    @Column(name = "ID")
    private Long id;
    @Email(message = "Email is not correct")
    @NotBlank
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @NotBlank
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "LOCALE")
    private String locale;
    @NotBlank
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ENABLED")
    private Integer enabled;
    @NotBlank
    @Column(name = "FULL_NAME")
    private String fullName;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<Role> authority = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthority();
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

        return getEnabled() == 1;
    }
}
