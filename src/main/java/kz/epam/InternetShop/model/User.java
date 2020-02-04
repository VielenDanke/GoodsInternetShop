package kz.epam.InternetShop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    @Column(name = "ID")
    private Long id;
    @Email(message = "Email is not correct")
    @NotBlank(message = "Username cannot be empty, you should use email as username")
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "LOCALE")
    private String locale;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ENABLED")
    private Integer enabled;
    @NotBlank
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "AUTH_PROVIDER")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    @Column(name = "PROVIDER_ID")
    private String providerId;
    @Column(name = "PICTURE")
    private String picture;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authority = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;
}
