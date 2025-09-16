package ch.schuum.backend.repository;

import ch.schuum.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Das UserRepository dient dazu, alle Daten der Domäne "Benutzer"
 * aus der Datenbank auszulesen. Die Implementation der Methoden ist durch
 * Hibernate/JPA gegeben
 *
 * @author Miriam Streit
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * <p>holt einen einzelnen Benutzer, dessen E-Mail-Adresse der mitgegebenen E-Mail-Adresse entspricht</p>
     * @param email E-Mail-Adresse, anhand welcher ein Benutzer gesucht wird
     * @return Optional mit dem Benutzerobjekt, oder leerer Optional, wenn kein Benutzer mit der E-Mail existiert
     * @author Miriam Streit
     */
    Optional<User> findByEmail(String email);
}
