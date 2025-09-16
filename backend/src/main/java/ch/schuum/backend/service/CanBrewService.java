package ch.schuum.backend.service;

import ch.schuum.backend.dto.BrewableDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.mapping.BeerTypeMapper;
import ch.schuum.backend.mapping.IngredientMapper;
import ch.schuum.backend.model.BeerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Der CanBrewService ist zuständig für die Business Logik zur Domäne "CanBrew"
 * und verbindet die Schichten Controller und Repository
 *
 * @author Miriam Streit
 *
 */
@Service
@RequiredArgsConstructor
public class CanBrewService {
    private final BeerTypeService beerTypeService;

    /**
     * <p>berechnet die Braubarkeit aller Biersorten. Wenn die benötigten Zutaten vorhanden sind,
     * ist die Sorte braubar, sonst werden die fehlenden Zutaten gesammelt.</p>
     * @return Liste von BrewableDto, die die Braubarkeit jeder Sorte enthalten
     * @author Nico Berchtold
     */
    public List<BrewableDto> getBrewableBeerTypes() {
        List<BeerType> beerTypes = BeerTypeMapper.INSTANCE.dtoListToModelList(beerTypeService.getAllBeerTypes());
        List<BrewableDto> brewableDtoBeers = new ArrayList<>();
        beerTypes.forEach(beerType -> {
            BrewableDto brewableDto = new BrewableDto();
            brewableDto.setName(beerType.getName());
            brewableDto.setBrewable(true);
            beerType.getIngredients().forEach(ingredientAmount -> {
                if (ingredientAmount.getAmount() > ingredientAmount.getIngredient().getStock()) {
                    brewableDto.setBrewable(false);
                    IngredientAmountDto missingIngredient = new IngredientAmountDto();
                    missingIngredient.setIngredient(IngredientMapper.INSTANCE.modelToDto(ingredientAmount.getIngredient()));
                    missingIngredient.setAmount(ingredientAmount.getAmount() - ingredientAmount.getIngredient().getStock());
                    brewableDto.getMissingIngredients().add(missingIngredient);
                }
            });

            brewableDtoBeers.add(brewableDto);
        });
        return brewableDtoBeers;
    }
}
