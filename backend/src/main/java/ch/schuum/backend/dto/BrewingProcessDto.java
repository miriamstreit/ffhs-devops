package ch.schuum.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Die Klasse BrewingProcessDto beschreibt alle nötigen Attribute eines
 * Brauprozesses als Data Transfer Object. Ebenfalls wird die Validierung der
 * Klasse beschrieben, welche vom Controller verwendet wird und Fehlermeldungen
 * erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.BrewingProcess}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrewingProcessDto {

    private int brewingId;

    @NotNull
    private BeerTypeDto beerType;

    @NotNull
    @JsonIgnoreProperties("brewingProcess")
    private TankDto tank;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private String sudNumber;

    private String comment;

    @NotNull
    private String color;

    private boolean deleted;
}
