package ch.schuum.backend.controller;

import ch.schuum.backend.dto.BrewableDto;
import ch.schuum.backend.service.CanBrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Der TankController nimmt alle REST-Abfragen für /canbrew entgegen, validiert und verarbeitet sie weiter.
 * Dabei ist er zuständig für alle Abfragen zum CanBrew Feature.
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@RestController
public class CanBrewController {
    private final CanBrewService canBrewService;

    /**
     * <p>holt die Braubarkeit aller Biersorten als Liste</p>
     * @return eine Liste mit Braubarkeit-Objekten, die zu einer Biersorte gehören
     * @author Nico Berchtold
     */
    @GetMapping("/canbrew")
    List<BrewableDto> getBrewableBeerTypes() {
        return canBrewService.getBrewableBeerTypes();
    }
}
