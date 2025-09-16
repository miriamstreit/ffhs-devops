package ch.schuum.backend.repository;

import ch.schuum.backend.model.BrewingProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Das BrewingProcessRepository dient dazu, alle Daten der Domäne "Brauprozess"
 * aus der Datenbank auszulesen. Die Implementation der Methoden ist durch
 * Hibernate/JPA gegeben
 *
 * @author Miriam Streit
 *
 */
@Repository
public interface BrewingProcessRepository extends JpaRepository<BrewingProcess, Integer> {
    /**
     * <p>holt alle Einträge, bei denen das deleted Attribut dem gewünschten Kriterium entspricht</p>
     * @param deleted boolean, der bestimmt, ob alle gelöschten oder alle nicht gelöschten Einträge geholt werden sollen
     * @return die gewünschte Menge als Liste von Brauprozessen
     * @author Nico Berchtold
     */
    List<BrewingProcess> findAllByDeleted(boolean deleted);

    /**
     * <p>holt alle Einträge, bei denen die ID des Tanks, das Enddatum und das Deleted Attribut den gewünschten Kriterien entsprechen</p>
     * @param tankId ID des Tanks für welchen Brauprozesse gesucht werden
     * @param currentDate Datumswert, der kleiner sein soll als das Enddatum
     * @param deleted boolean, der bestimmt, ob alle gelöschten oder alle nicht gelöschten Einträge geholt werden sollen
     * @return die gewünschte Menge als Liste von Brauprozessen
     * @author Nico Berchtold
     */
    Optional<BrewingProcess> findFirstByTank_TankIdAndEndDateGreaterThanEqualAndDeleted(Integer tankId, LocalDate currentDate, boolean deleted);

    /**
     * <p>holt alle Einträge, bei denen die ID des Tanks und das Deleted Attribut den gewünschten Kriterien entsprechen</p>
     * @param tankId ID des Tanks für welchen Brauprozesse gesucht werden
     * @param deleted boolean, der bestimmt, ob alle gelöschten oder alle nicht gelöschten Einträge geholt werden sollen
     * @return die gewünschte Menge als Liste von Brauprozessen
     * @author Nico Berchtold
     */
    List<BrewingProcess> findByTank_TankIdAndDeleted(Integer tankId, boolean deleted);
}
