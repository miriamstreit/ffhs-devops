package ch.schuum.backend.repository;

import ch.schuum.backend.model.BeerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Das BeerTypeRepository dient dazu, alle Daten der Domäne "Biersorte"
 * aus der Datenbank auszulesen. Die Implementation der Methoden ist durch
 * Hibernate/JPA gegeben
 *
 * @author Miriam Streit
 *
 */
@Repository
public interface BeerTypeRepository extends JpaRepository<BeerType, Integer> {

    /**
     * <p>holt alle Einträge, bei denen das deleted Attribut dem gewünschten Kriterium entspricht</p>
     * @param deleted boolean, der bestimmt, ob alle gelöschten oder alle nicht gelöschten Einträge geholt werden sollen
     * @return die gewünschte Menge als Liste von Biersorten
     * @author Nico Berchtold
     */
    List<BeerType> findAllByDeleted(boolean deleted);
}
