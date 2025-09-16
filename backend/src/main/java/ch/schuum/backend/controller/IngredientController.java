package ch.schuum.backend.controller;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Der IngredientController nimmt alle REST-Abfragen für /tanks entgegen, validiert und verarbeitet sie weiter.
 * Dabei ist er zuständig für alle Abfragen zu Zutaten.
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@RestController
public class IngredientController {
    private final IngredientService ingredientService;

    /**
     * <p>holt alle Zutaten</p>
     * @return eine Liste von Zutaten
     * @author Florian Susuri
     */
    @GetMapping("/ingredients")
    List<IngredientDto> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    /**
     * <p>holt alle Zutaten, bei welchen der Lagerbestand unter dem Schwellwert liegt</p>
     * @return eine Liste von Zutaten
     * @author Florian Susuri
     */
    @GetMapping("/ingredients/belowthreshold")
    List<IngredientDto> getAllIngredientsBelowThreshold() {
        return ingredientService.getAllIngredientsBelowThreshold();
    }

    /**
     * <p>holt eine Zutat anhand ihrer ID</p>
     * @param id ID der gesuchten Zutat
     * @return die gesuchte Zutat
     * @throws ResponseStatusException, falls die ID in der Datenbank nicht existiert
     * @author Florian Susuri
     */
    @GetMapping("/ingredients/{id}")
    IngredientDto getIngredientById(@PathVariable int id) {
        return ingredientService.getIngredientById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Zutat mit der ID " + id + " konnte nicht gefunden werden."
        ));
    }

    /**
     * <p>erstellt eine neue Zutat</p>
     * @param ingredient alle Daten der neu zu erstellenden Zutat
     * @return die erstellte Zutat
     * @author Florian Susuri
     */
    @PostMapping("/ingredients")
    IngredientDto saveIngredient(@RequestBody IngredientDto ingredient) {
        return ingredientService.createIngredient(ingredient);
    }

    /**
     * <p>aktualisiert eine bestehende Zutat</p>
     * @param id ID der zu aktualisierenden Zutat
     * @param ingredient alle Daten der zu aktualisierenden Zutat
     * @return die aktualisierte Zutat
     * @author Florian Susuri
     */
    @PutMapping("/ingredients/{id}")
    IngredientDto saveIngredient(@PathVariable int id, @RequestBody IngredientDto ingredient) {
        return ingredientService.updateIngredient(id, ingredient);
    }

    /**
     * <p>setzt eine bestehende Zutat auf gelöscht</p>
     * @param id die ID der zu löschenden Zutat
     * @return die ID der gelöschten Zutat
     * @author Florian Susuri
     */
    @DeleteMapping("/ingredients/{id}")
    int deleteIngredient(@PathVariable int id) {
        return ingredientService.deleteIngredient(id);
    }
}
