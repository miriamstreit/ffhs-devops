package ch.schuum.backend.service;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.mapping.BeerTypeMapper;
import ch.schuum.backend.mapping.IngredientAmountMapper;
import ch.schuum.backend.model.BeerType;
import ch.schuum.backend.repository.BeerTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Der BeerTypeService ist zuständig für die Businesslogik zur Domäne "Biersorte"
 * und verbindet die Schichten Controller und Repository
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@Service
public class BeerTypeService {
    private final BeerTypeRepository beerTypeRepository;

    /**
     * <p>holt alle Biersorten</p>
     * @return alle nicht gelöschten Biersorten
     * @author Nico Berchtold
     */
    public List<BeerTypeDto> getAllBeerTypes() {
        List<BeerType> types = beerTypeRepository.findAllByDeleted(false);
        return BeerTypeMapper.INSTANCE.modelListToDtoList(types);
    }

    /**
     * <p>holt eine Biersorte mit ihrer ID</p>
     * @param id ID der gesuchten Biersorte
     * @return die gesuchte Biersorte als Optional oder leerer Optional
     * @author Nico Berchtold
     */
    public Optional<BeerTypeDto> getBeerTypeById(int id) {
        Optional<BeerType> beerTypeModel = beerTypeRepository.findById(id);
        if (beerTypeModel.isPresent()) {
            return Optional.of(BeerTypeMapper.INSTANCE.modelToDto(beerTypeModel.get()));
        }
        return Optional.empty();
    }

    /**
     * <p>speichert eine neue Biersorte</p>
     * @param beerType die neue, zu speichernde Biersorte
     * @return die gespeicherte Biersorte
     * @author Nico Berchtold
     */
    public BeerTypeDto createBeerType(BeerTypeDto beerType) {
        BeerType beerTypeModel = BeerTypeMapper.INSTANCE.dtoToModel(beerType);
        BeerType savedObject = beerTypeRepository.save(beerTypeModel);
        return BeerTypeMapper.INSTANCE.modelToDto(savedObject);
    }

    /**
     * <p>aktualisiert eine bestehende Biersorte</p>
     * <p>wenn die Biersorte nicht existiert, wird sie stattdessen als neue gespeichert</p>
     * @param id die ID der zu aktualisierenden Biersorte
     * @param beerType die aktualisierte Biersorte
     * @return die aktualisierte Biersorte
     * @author Nico Berchtold
     */
    public BeerTypeDto updateBeerType(int id, BeerTypeDto beerType) {
        Optional<BeerType> existingBeerType = beerTypeRepository.findById(id);
        if (existingBeerType.isPresent()) {
            existingBeerType.get().setName(beerType.getName());
            existingBeerType.get().setDuration(beerType.getDuration());
            existingBeerType.get().getIngredients().clear();
            existingBeerType.get().getIngredients().addAll(IngredientAmountMapper.INSTANCE.dtoListToModelList(beerType.getIngredients()));
            return BeerTypeMapper.INSTANCE.modelToDto(beerTypeRepository.save(existingBeerType.get()));
        }
        return createBeerType(beerType);
    }

    /**
     * <p>setzt eine Biersorte auf gelöscht</p>
     * <p>wenn die Biersorte nicht existiert, wird nichts gelöscht</p>
     * @param id die ID der zu löschenden Biersorte
     * @return die ID der gelöschten Biersorte oder 0
     * @author Nico Berchtold
     */
    public int deleteBeerType(int id) {
        Optional<BeerType> existingBeerType = beerTypeRepository.findById(id);
        if (existingBeerType.isPresent()) {
            existingBeerType.get().setDeleted(true);
            beerTypeRepository.save(existingBeerType.get());
            return id;
        }
        return 0;
    }
}
