package ch.schuum.backend.controller;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.service.BeerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Der BeerTypeController nimmt alle REST-Abfragen für /tanks entgegen, validiert und verarbeitet sie weiter.
 * Dabei ist er zuständig für alle Abfragen zu Biersorten.
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@RestController
public class BeerTypeController {
    private final BeerTypeService beerTypeService;


    /**
     * <p>holt alle Biersorten</p>
     * @return eine Liste von Biersorten
     * @author Nico Berchtold
     */
    @GetMapping("/beertypes")
    List<BeerTypeDto> getAllBeerTypes() {
        return beerTypeService.getAllBeerTypes();
    }

    /**
     * <p>holt eine Biersorte anhand ihrer ID</p>
     * @param id ID der gesuchten Biersorte
     * @return die gesuchte Biersorte
     * @throws ResponseStatusException, falls die ID in der Datenbank nicht existiert
     * @author Nico Berchtold
     */
    @GetMapping("/beertypes/{id}")
    BeerTypeDto getBeerTypeById(@PathVariable int id) {
        return beerTypeService.getBeerTypeById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Biersorte mit der ID " + id + " konnte nicht gefunden werden."
        ));
    }

    /**
     * <p>erstellt eine neue Biersorte</p>
     * @param beerType alle Daten der neu zu erstellenden Biersorte
     * @return die erstellte Biersorte
     * @author Nico Berchtold
     */
    @PostMapping("/beertypes")
    BeerTypeDto saveBeerType(@RequestBody BeerTypeDto beerType) {
        return beerTypeService.createBeerType(beerType);
    }

    /**
     * <p>aktualisiert eine bestehende Biersorte</p>
     * @param id ID der zu aktualisierenden Biersorte
     * @param beerType alle Daten der zu aktualisierenden Biersorte
     * @return die aktualisierte Biersorte
     * @author Nico Berchtold
     */
    @PutMapping("/beertypes/{id}")
    BeerTypeDto saveBeerType(@PathVariable int id, @RequestBody BeerTypeDto beerType) {
        return beerTypeService.updateBeerType(id, beerType);
    }

    /**
     * <p>setzt eine bestehende Biersorte auf gelöscht</p>
     * @param id die ID der zu löschenden Biersorte
     * @return die ID der gelöschten Biersorte
     * @author Nico Berchtold
     */
    @DeleteMapping("/beertypes/{id}")
    int deleteBeerType(@PathVariable int id) {
        return beerTypeService.deleteBeerType(id);
    }
}
