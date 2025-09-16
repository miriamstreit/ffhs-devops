package ch.schuum.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Die Klasse TankDto beschreibt alle nötigen Attribute eines
 * Tanks als Data Transfer Object. Ebenfalls wird die Validierung der
 * Klasse beschrieben, welche vom Controller verwendet wird und Fehlermeldungen
 * erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.Tank}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TankDto {

    private int tankId;

    @NotNull
    private int volume;

    @JsonIgnoreProperties("tank")
    private BrewingProcessDto brewingProcess;
}
