package ch.schuum.backend.auth;

import ch.schuum.backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


/**
 * Die Klasse UserDetailsImpl implementiert {@link org.springframework.security.core.userdetails.UserDetails}
 * und beschreibt die Benutzer Konfiguration für die Authentisierung.
 *
 * @author Miriam Streit
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
    private Long id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;


    /**
     * <p>Builder Funktion für User-Objekt</p>
     * @param user Benutzer mit allen benötigten Daten
     * @return UserDetailsImpl Objekt mit den Daten des Benutzers
     * @author Miriam Streit
     */
    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                (long) user.getUserId(),
                user.getEmail(),
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList());
    }

    /**
     * <p>gibt die Authorities des Benutzers zurück</p>
     * @return Liste von Authorities
     * @author Miriam Streit
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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