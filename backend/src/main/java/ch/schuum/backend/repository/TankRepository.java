package ch.schuum.backend.repository;

import ch.schuum.backend.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Das TankRepository dient dazu, alle Daten der Domäne "Tank"
 * aus der Datenbank auszulesen. Die Implementation der Methoden ist durch
 * Hibernate/JPA gegeben
 *
 * @author Miriam Streit
 *
 */
@Repository
public interface TankRepository extends JpaRepository<Tank, Integer> {
}
