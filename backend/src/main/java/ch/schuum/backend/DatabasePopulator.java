package ch.schuum.backend;

import ch.schuum.backend.model.*;
import ch.schuum.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DatabasePopulator befüllt die Datenbank beim Start der Applikation,
 * falls sie noch leer ist. Wenn die Tabellen "ingredients" und "tanks" leer sind,
 * wird davon ausgegangen, dass noch keine Daten existieren, und es werden Daten eingefügt.
 * Für die Daten werden Tanks, Zutaten, Biersorten, Brauprozesse und ein Benutzer eingefügt.
 *
 * @author Miriam Streit
 *
 */
@Component
@RequiredArgsConstructor
public class DatabasePopulator {
    private final BeerTypeRepository beerTypeRepository;
    private final BrewingProcessRepository brewingProcessRepository;
    private final IngredientRepository ingredientRepository;
    private final TankRepository tankRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(DatabasePopulator.class);

    /**
     * <p>befüllt die Datenbank, falls noch keine Einträge vorhanden sind</p>
     * @author Miriam Streit
     */
    @EventListener(ApplicationReadyEvent.class)
    public void populateDBIfNotExists() {
        if (!tankRepository.findAll().isEmpty() && !ingredientRepository.findAll().isEmpty()) {
            logger.info("database has entries and will not be reinitialized");
            return;
        }

        logger.info("database is empty and will be initialized");

        Tank tank1 = new Tank(1, 50);
        Tank tank2 = new Tank(2, 30);
        Tank tank3 = new Tank(3, 40);
        Tank tank4 = new Tank(4, 67);
        Tank tank5 = new Tank(5, 13);
        Tank tank6 = new Tank(6, 50);
        Tank tank7 = new Tank(7, 37);
        Tank tank8 = new Tank(8, 80);
        List<Tank> tanks = new ArrayList<>();
        Collections.addAll(tanks, tank1, tank2, tank3, tank4, tank5, tank6, tank7, tank8);
        tankRepository.saveAll(tanks);

        Ingredient ingredient1 = new Ingredient(1, "Weizen", 34.0F, 50.0F, false);
        Ingredient ingredient2 = new Ingredient(2, "Hopfen", 356.0F, 50.0F, false);
        Ingredient ingredient3 = new Ingredient(3, "Malz", 75.0F, 50.0F, false);
        ingredient1 = ingredientRepository.save(ingredient1);
        ingredient2 = ingredientRepository.save(ingredient2);
        ingredient3 = ingredientRepository.save(ingredient3);

        IngredientAmount ingredientAmount1 = new IngredientAmount(ingredient1, 45F);
        IngredientAmount ingredientAmount2 = new IngredientAmount(ingredient2, 32F);
        IngredientAmount ingredientAmount3 = new IngredientAmount(ingredient3, 12F);
        List<IngredientAmount> ingredientAmounts = new ArrayList<>();
        Collections.addAll(ingredientAmounts, ingredientAmount1, ingredientAmount2, ingredientAmount3);

        BeerType beerType1 = new BeerType(1, "Lager", 30, Collections.emptyList(), false);
        BeerType beerType2 = new BeerType(2, "IPA", 30, ingredientAmounts, false);
        beerType1 = beerTypeRepository.save(beerType1);
        beerType2 = beerTypeRepository.save(beerType2);

        BrewingProcess brewingProcess1 = new BrewingProcess(1, beerType1, tank2, LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), "12434", "no comment", "blue", false);
        BrewingProcess brewingProcess2 = new BrewingProcess(2, beerType2, tank3, LocalDate.now(), LocalDate.now().plusDays(30), "46457", "no comment 2", "red", false);
        List<BrewingProcess> brewingProcesses = new ArrayList<>();
        brewingProcesses.add(brewingProcess1);
        brewingProcesses.add(brewingProcess2);
        brewingProcessRepository.saveAll(brewingProcesses);
 
        // SchuumB1erTheB35t*
        User user = new User(1, "admin@schuum.ch", "$2a$12$pdf23RNAOWcK0okRznqwHujwUtySih43R6GnPdjWgOgmcJe4QJi9e", null);
        userRepository.save(user);
    }
}
