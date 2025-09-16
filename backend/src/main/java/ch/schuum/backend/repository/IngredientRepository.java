package ch.schuum.backend.repository;

import ch.schuum.backend.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Das IngredientRepository dient dazu, alle Daten der Domäne "Zutaten"
 * aus der Datenbank auszulesen. Die Implementation der Methoden ist durch
 * Hibernate/JPA gegeben
 *
 * @author Miriam Streit
 *
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    /**
     * <p>holt alle Zutaten, bei welchen ihr Lagerbestand unter ihrem Schwellwert liegt</p>
     * @return die gewünschte Menge als Liste von Zutaten
     * @author Florian Susuri
     */
    @Query(value = "SELECT * FROM ingredient WHERE stock < threshold AND deleted = false", nativeQuery = true)
    List<Ingredient> findIngredientsBelowThreshold();

    /**
     * <p>holt alle Einträge, bei denen das deleted Attribut dem gewünschten Kriterium entspricht</p>
     * @param deleted boolean, der bestimmt, ob alle gelöschten oder alle nicht gelöschten Einträge geholt werden sollen
     * @return die gewünschte Menge als Liste von Zutaten
     * @author Florian Susuri
     */
    List<Ingredient> findAllByDeleted(boolean deleted);
}
