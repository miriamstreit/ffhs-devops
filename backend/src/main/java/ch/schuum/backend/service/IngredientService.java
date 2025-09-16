package ch.schuum.backend.service;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.mapping.IngredientMapper;
import ch.schuum.backend.model.Ingredient;
import ch.schuum.backend.repository.IngredientRepository;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Der IngredientService ist zuständig für die Business Logik zur Domäne "Zutaten"
 * und verbindet die Schichten Controller und Repository
 *
 * @author Miriam Streit
 *
 */
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    private AtomicInteger ingredientsBelowThresholdGauge;

    public IngredientService(IngredientRepository ingredientRepository, MeterRegistry meterRegistry) {
        this.ingredientRepository = ingredientRepository;

        // gauge
        this.ingredientsBelowThresholdGauge = meterRegistry.gauge("ingredients_below_threshold_per_minute", new AtomicInteger(0));
    }
    /**
     * <p>holt alle Zutaten</p>
     * @return alle nicht gelöschten Zutaten
     * @author Florian Susuri
     */
    public List<IngredientDto> getAllIngredients() {
        return IngredientMapper.INSTANCE.modelListToDtoList(ingredientRepository.findAllByDeleted(false));
    }

    /**
     * <p>holt alle Zutaten, bei welchen der Lagerbestand unter ihrem Schwellwert liegt</p>
     * @return eine Liste der Zutaten, bei welchen der Lagerbestand unter dem Schwellwert liegt
     * @author Florian Susuri
     */
    public List<IngredientDto> getAllIngredientsBelowThreshold() {
        return IngredientMapper.INSTANCE.modelListToDtoList(ingredientRepository.findIngredientsBelowThreshold());
    }

    @Scheduled(fixedRate = 60000)
    public void measureIngredientsBelowThreshold() {
        List<IngredientDto> ingredientsBelowThreshold = getAllIngredientsBelowThreshold();
        ingredientsBelowThresholdGauge.set(ingredientsBelowThreshold.size());
    }

    /**
     * <p>holt eine Zutat mit ihrer ID</p>
     * @param id ID der gesuchten Zutat
     * @return die gesuchte Zutat als Optional oder leerer Optional
     * @author Florian Susuri
     */
    public Optional<IngredientDto> getIngredientById(int id) {
        Optional<Ingredient> beerTypeModel = ingredientRepository.findById(id);
        if (beerTypeModel.isPresent()) {
            return Optional.of(IngredientMapper.INSTANCE.modelToDto(beerTypeModel.get()));
        }
        return Optional.empty();
    }

    /**
     * <p>speichert eine neue Zutat</p>
     * @param ingredient die neue, zu speichernde Zutat
     * @return die gespeicherte Zutat
     * @author Florian Susuri
     */
    public IngredientDto createIngredient(IngredientDto ingredient) {
        Ingredient ingredientModel = IngredientMapper.INSTANCE.dtoToModel(ingredient);
        Ingredient savedObject = ingredientRepository.save(ingredientModel);
        return IngredientMapper.INSTANCE.modelToDto(savedObject);
    }

    /**
     * <p>aktualisiert eine bestehende Zutat</p>
     * <p>wenn die Zutat nicht existiert, wird sie stattdessen als neue gespeichert</p>
     * @param id die ID der zu aktualisierenden Zutat
     * @param ingredient die aktualisierte Zutat
     * @return die aktualisierte Zutat
     * @author Florian Susuri
     */
    public IngredientDto updateIngredient(int id, IngredientDto ingredient) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            existingIngredient.get().setName(ingredient.getName());
            existingIngredient.get().setStock(ingredient.getStock());
            existingIngredient.get().setThreshold(ingredient.getThreshold());
            return IngredientMapper.INSTANCE.modelToDto(ingredientRepository.save(existingIngredient.get()));
        }
        return createIngredient(ingredient);
    }

    /**
     * <p>setzt eine Zutat auf gelöscht</p>
     * <p>wenn die Zutat nicht existiert, wird nichts gelöscht</p>
     * @param id die ID der zu löschenden Zutat
     * @return die ID der gelöschten Zutat oder 0
     * @author Florian Susuri
     */
    public int deleteIngredient(int id) {
        Optional<Ingredient> existingIngredient = ingredientRepository.findById(id);
        if (existingIngredient.isPresent()) {
            existingIngredient.get().setDeleted(true);
            ingredientRepository.save(existingIngredient.get());
            return id;
        }
        return 0;
    }
}
