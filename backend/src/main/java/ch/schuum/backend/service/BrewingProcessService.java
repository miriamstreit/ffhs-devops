package ch.schuum.backend.service;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.dto.BrewingProcessDto;
import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.mapping.BrewingProcessMapper;
import ch.schuum.backend.model.BrewingProcess;
import ch.schuum.backend.repository.BrewingProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Der BrewingProcessService ist zuständig für die Business Logik zur Domäne "Brauprozess"
 * und verbindet die Schichten Controller und Repository
 *
 * @author Miriam Streit
 */
@Service
@RequiredArgsConstructor
public class BrewingProcessService {
    private final BrewingProcessRepository brewingProcessRepository;
    private final IngredientService ingredientService;
    private final BeerTypeService beerTypeService;

    /**
     * <p>holt alle Brauprozesse</p>
     *
     * @return alle nicht gelöschten Brauprozesse
     * @author Nico Berchtold
     */
    public List<BrewingProcessDto> getAllBrewingProcesses() {
        List<BrewingProcess> brewingProcesses = brewingProcessRepository.findAllByDeleted(false);
        return BrewingProcessMapper.INSTANCE.modelListToDtoList(brewingProcesses);
    }

    /**
     * <p>holt einen Brauprozess mit seiner ID</p>
     *
     * @param id ID des gesuchten Brauprozesses
     * @return den gesuchten Brauprozess als Optional oder leerer Optional
     * @author Nico Berchtold
     */
    public Optional<BrewingProcessDto> getBrewingProcessById(int id) {
        Optional<BrewingProcess> brewingProcessModel = brewingProcessRepository.findById(id);
        if (brewingProcessModel.isPresent()) {
            return Optional.of(BrewingProcessMapper.INSTANCE.modelToDto(brewingProcessModel.get()));
        }
        return Optional.empty();
    }

    /**
     * <p>speichert einen neuen Brauprozess</p>
     * <p>bei der Erstellung werden die benötigten Zutaten der Biersorte dem Inventar abgezogen</p>
     * <p>es wird geprüft, ob die vorhandenen Zutaten ausreichen und ob der Tank noch nicht besetzt ist</p>
     *
     * @param brewingProcess der neue, zu speichernde Brauprozess
     * @return der gespeicherte Brauprozess
     * @throws ResponseStatusException bei nicht ausreichenden Zutaten
     * @throws ResponseStatusException bei bereits besetztem Tank
     * @author Nico Berchtold
     */
    public BrewingProcessDto createBrewingProcess(BrewingProcessDto brewingProcess) {
        Optional<BeerTypeDto> beerTypeDto = beerTypeService.getBeerTypeById(brewingProcess.getBeerType().getBeerTypeId());
        if (beerTypeDto.isPresent()) {
            if (isTankInUse(brewingProcess)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Dieser Tank wird zu diesem Zeitpunkt bereits verwendet."
                );
            }

            List<IngredientAmountDto> ingredientAmountDtos = beerTypeDto.get().getIngredients();
            Map<Integer, IngredientDto> ingredientsMap = new HashMap<>();
            ingredientAmountDtos.forEach(ingredientAmount -> {
                Optional<IngredientDto> ingredient = ingredientService.getIngredientById(ingredientAmount.getIngredient().getIngredientId());
                if (ingredient.isPresent()) {
                    if (ingredient.get().getStock() >= ingredientAmount.getAmount()) {
                        IngredientDto subtractedIngredient = ingredient.get();
                        subtractedIngredient.setStock(ingredient.get().getStock() - ingredientAmount.getAmount());
                        ingredientsMap.put(ingredientAmount.getIngredient().getIngredientId(), ingredient.get());
                    } else {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Die benötigten Zutaten reichen nicht aus für diese Aktion."
                        );
                    }
                }
            });

            ingredientsMap.values().forEach(ingredientDto -> ingredientService.updateIngredient(ingredientDto.getIngredientId(), ingredientDto));

            BrewingProcess brewingProcessModel = BrewingProcessMapper.INSTANCE.dtoToModel(brewingProcess);
            brewingProcessModel.setEndDate(brewingProcess.getStartDate().plusDays(beerTypeDto.get().getDuration()));
            return BrewingProcessMapper.INSTANCE.modelToDto(brewingProcessRepository.save(brewingProcessModel));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Die Biersorte wurde nicht spezifiziert."
            );
        }
    }

    /**
     * <p>aktualisiert einen bestehenden Brauprozess</p>
     * <p>Der Tank und die Biersorte können nicht mehr angepasst werden<p/>
     * <p>wenn der Brauprozess nicht existiert, wird sie stattdessen als neue gespeichert</p>
     *
     * @param id             die ID des zu aktualisierenden Brauprozesses
     * @param brewingProcess der aktualisierte Brauprozess
     * @return der aktualisierte Brauprozess
     * @author Nico Berchtold
     */
    public BrewingProcessDto updateBrewingProcess(int id, BrewingProcessDto brewingProcess) {
        Optional<BrewingProcess> existingBrewingProcess = brewingProcessRepository.findById(id);
        if (existingBrewingProcess.isPresent()) {
            // BeerType und Tank wird absichtlich nicht aktualisiert, diese Änderung darf nicht möglich sein
            existingBrewingProcess.get().setColor(brewingProcess.getColor());
            existingBrewingProcess.get().setComment(brewingProcess.getComment());
            existingBrewingProcess.get().setStartDate(brewingProcess.getStartDate());
            // Enddatum wird automatisch berechnet
            LocalDate endDate = brewingProcess.getStartDate()
                    .plusDays(existingBrewingProcess.get().getBeerType().getDuration());
            existingBrewingProcess.get().setEndDate(endDate);
            existingBrewingProcess.get().setSudNumber(brewingProcess.getSudNumber());

            BrewingProcess savedObject = brewingProcessRepository.save(existingBrewingProcess.get());
            return BrewingProcessMapper.INSTANCE.modelToDto(savedObject);
        }
        return createBrewingProcess(brewingProcess);
    }

    /**
     * <p>setzt einen Brauprozess auf gelöscht</p>
     * <p>die bei der Erstellung beim Inventar abgezogenen Zutaten werden wieder hinzugefügt</p>
     * <p>wenn der Brauprozess nicht existiert, wird nichts gelöscht</p>
     *
     * @param id die ID des zu löschenden Brauprozesses
     * @return die ID des gelöschten Brauprozesses oder 0
     * @author Nico Berchtold
     */
    public int deleteBrewingProcess(int id) {
        Optional<BrewingProcess> existingBrewingProcess = brewingProcessRepository.findById(id);
        if (existingBrewingProcess.isPresent()) {
            existingBrewingProcess.get().getBeerType().getIngredients().forEach(ingredientAmount -> {
                Optional<IngredientDto> ingredient = ingredientService.getIngredientById(ingredientAmount.getIngredient().getIngredientId());
                if (ingredient.isPresent()) {
                    ingredient.get().setStock(ingredient.get().getStock() + ingredientAmount.getAmount());
                    ingredientService.updateIngredient(ingredient.get().getIngredientId(), ingredient.get());
                }
            });
            existingBrewingProcess.get().setDeleted(true);
            brewingProcessRepository.save(existingBrewingProcess.get());
            return id;
        }
        return 0;
    }

    /**
     * <p>holt einen aktuellen, nicht gelöschten Brauprozess anhand seines verwendeten Tanks</p>
     *
     * @param tankId die ID des Tanks
     * @return der zugehörige Brauprozess, falls vorhanden, sonst null
     * @author Nico Berchtold
     */
    public BrewingProcess getCurrentBrewingProcessByTankId(int tankId) {
        Optional<BrewingProcess> currentBrewingProcessOfTank = brewingProcessRepository.findFirstByTank_TankIdAndEndDateGreaterThanEqualAndDeleted(tankId, LocalDate.now(), false);
        return currentBrewingProcessOfTank.orElse(null);
    }

    private boolean isTankInUse(BrewingProcessDto brewingProcessDto) {
        List<BrewingProcess> brewingProcessesOfTank = brewingProcessRepository.findByTank_TankIdAndDeleted(brewingProcessDto.getTank().getTankId(), false);

        AtomicBoolean overlapping = new AtomicBoolean(false);
        brewingProcessesOfTank.forEach(bp -> {
            LocalDate s1 = brewingProcessDto.getStartDate();
            LocalDate e1 = brewingProcessDto.getEndDate();
            LocalDate s2 = bp.getStartDate();
            LocalDate e2 = bp.getEndDate();

            if (s1.isBefore(s2) && e1.isAfter(s2) ||
                    s1.isBefore(e2) && e1.isAfter(e2) ||
                    s1.isBefore(s2) && e1.isAfter(e2) ||
                    s1.isAfter(s2) && e1.isBefore(e2) ||
                    s1.isEqual(s2) || e1.isEqual(e2)) {
                overlapping.set(true);
            }
        });
        return overlapping.get();
    }
}
