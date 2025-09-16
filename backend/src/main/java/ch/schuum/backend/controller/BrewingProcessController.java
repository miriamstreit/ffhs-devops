package ch.schuum.backend.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import ch.schuum.backend.dto.BrewingProcessDto;
import ch.schuum.backend.service.BrewingProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Der BrewingProcessController nimmt alle REST-Abfragen für /tanks entgegen, validiert und verarbeitet sie weiter.
 * Dabei ist er zuständig für alle Abfragen zu Brauprozessen.
 *
 * @author Miriam Streit
 *
 */
@RestController
public class BrewingProcessController {
    private final BrewingProcessService brewingProcessService;

    private Counter brewingProcessCounter;

    public BrewingProcessController(BrewingProcessService brewingProcessService, MeterRegistry meterRegistry) {
        this.brewingProcessService = brewingProcessService;
        // counter
        this.brewingProcessCounter = Counter.builder("brewing_process_creation_request_total").
                tag("version", "v1").
                description("Brewing Process Creation Count").
                register(meterRegistry);
    }

    /**
     * <p>holt alle Brauprozesse</p>
     * @return eine Liste von Brauprozessen
     * @author Nico Berchtold
     */
    @GetMapping("/brewingprocess")
    List<BrewingProcessDto> getAllBrewingProcesses() {
        return brewingProcessService.getAllBrewingProcesses();
    }

    /**
     * <p>holt einen Brauprozess anhand seiner ID</p>
     * @param id ID des gesuchten Brauprozesses
     * @return der gesuchte Brauprozess
     * @throws ResponseStatusException, falls die ID in der Datenbank nicht existiert
     * @author Nico Berchtold
     */
    @GetMapping("/brewingprocess/{id}")
    BrewingProcessDto getBrewingProcessById(@PathVariable int id) {
        return brewingProcessService.getBrewingProcessById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Brauprozess mit der ID " + id + " konnte nicht gefunden werden."
        ));
    }

    /**
     * <p>erstellt einen neuen Brauprozess</p>
     * @param brewingProcess alle Daten des neu zu erstellenden Brauprozesses
     * @return der erstellte Brauprozess
     * @author Nico Berchtold
     */
    @PostMapping("/brewingprocess")
    BrewingProcessDto saveBrewingProcess(@RequestBody BrewingProcessDto brewingProcess) {
        brewingProcessCounter.increment();
        return brewingProcessService.createBrewingProcess(brewingProcess);
    }

    /**
     * <p>aktualisiert einen bestehenden Brauprozess</p>
     * @param id ID des zu aktualisierenden Brauprozesses
     * @param brewingProcess alle Daten des zu aktualisierenden Brauprozesses
     * @return der aktualisierte Brauprozess
     * @author Nico Berchtold
     */
    @PutMapping("/brewingprocess/{id}")
    BrewingProcessDto saveBrewingProcess(@PathVariable int id, @RequestBody BrewingProcessDto brewingProcess) {
        return brewingProcessService.updateBrewingProcess(id, brewingProcess);
    }

    /**
     * <p>setzt einen bestehenden Brauprozess auf gelöscht</p>
     * @param id die ID des zu löschenden Brauprozesses
     * @return die ID des gelöschten Brauprozesses
     * @author Nico Berchtold
     */
    @DeleteMapping("/brewingprocess/{id}")
    int deleteBrewingProcess(@PathVariable int id) {
        return brewingProcessService.deleteBrewingProcess(id);
    }
}
