package ch.schuum.backend.service;

import ch.schuum.backend.auth.UserDetailsImpl;
import ch.schuum.backend.model.User;
import ch.schuum.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Der UserDetailsSeviceImpl ist zuständig für die Business Logik zur Domäne "User"
 * und verbindet die Schichten Controller und Repository
 * Dabei wird das Interface {@link org.springframework.security.core.userdetails.UserDetailsService} von Spring Security
 * implementiert, das dann für die Authentisierungslogik benötigt wird
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * <p>holt einen Benutzer anhand seiner E-Mail</p>
     * @param email E-Mail des gesuchten Nutzers (dient als Username)
     * @return den gesuchten Benutzer als UserDetails Objekt
     * @throws UsernameNotFoundException wenn der Benutzer nicht existiert
     * @author Miriam Streit
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden."));

        return UserDetailsImpl.build(user);
    }

}
