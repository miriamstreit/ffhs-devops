package ch.schuum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Die Klasse IngredientDto beschreibt alle nötigen Attribute einer
 * Zutat als Data Transfer Object. Ebenfalls wird die Validierung der
 * Klasse beschrieben, welche vom Controller verwendet wird und Fehlermeldungen
 * erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.Ingredient}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {

    private int ingredientId;

    @NotNull
    private String name;

    @NotNull
    private float stock;

    @NotNull
    private float threshold;

    private boolean deleted;
}
