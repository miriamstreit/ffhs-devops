package ch.schuum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Die Klasse BeerTypeDto beschreibt alle nötigen Attribute einer
 * Biersorte als Data Transfer Object. Ebenfalls wird die Validierung der
 * Klasse beschrieben, welche vom Controller verwendet wird und Fehlermeldungen
 * erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.BeerType}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerTypeDto {

    private int beerTypeId;

    @NotNull
    private String name;

    @NotNull
    private int duration;

    @NotNull
    @NotEmpty
    private List<IngredientAmountDto> ingredients;

    private boolean deleted;
}
