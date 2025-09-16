package ch.schuum.backend.controller;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.service.TankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Der TankController nimmt alle REST-Abfragen für /tanks entgegen, validiert und verarbeitet sie weiter.
 * Dabei ist er zuständig für alle Abfragen zu Tanks.
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@RestController
public class TankController {
    private final TankService tankService;

    /**
     * <p>holt alle Tanks</p>
     * @return eine Liste von Tanks
     * @author Florian Susuri
     */
    @GetMapping("/tanks")
    List<TankDto> getAllTanks() {
        return tankService.getAllTanks();
    }

    /**
     * <p>holt einen Tank anhand seiner ID</p>
     * @param id ID des gesuchten Tanks
     * @return der gesuchte Tank
     * @throws ResponseStatusException, falls die ID in der Datenbank nicht existiert
     * @author Florian Susuri
     */
    @GetMapping("/tanks/{id}")
    TankDto getTankById(@PathVariable int id) {
        return tankService.getTankById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tank mit der ID " + id + " konnte nicht gefunden werden."
        ));
    }

    /**
     * <p>erstellt einen neuen Tank</p>
     * @param tank alle Daten des neu zu erstellenden Tanks
     * @return der erstellte Tank
     * @author Florian Susuri
     */
    @PostMapping("/tanks")
    TankDto saveTank(@RequestBody TankDto tank) {
        return tankService.createTank(tank);
    }

    /**
     * <p>aktualisiert einen bestehenden Tank</p>
     * @param id ID des zu aktualisierenden Tanks
     * @param tank alle Daten des zu aktualisierenden Tanks
     * @return der aktualisierte Tank
     * @author Florian Susuri
     */
    @PutMapping("/tanks/{id}")
    TankDto saveTank(@PathVariable int id, @RequestBody TankDto tank) {
        return tankService.updateTank(id, tank);
    }
}
