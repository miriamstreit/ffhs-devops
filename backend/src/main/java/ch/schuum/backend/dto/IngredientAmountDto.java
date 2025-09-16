package ch.schuum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Die Klasse IngredientAmount umfasst eine Zutat und die zugehörige Menge,
 * die im Rezept einer Biersorte benötigt werden, als Data Transfer Object.
 * Ebenfalls wird die Validierung der Klasse beschrieben, welche vom Controller
 * verwendet wird und Fehlermeldungen erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.IngredientAmount}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientAmountDto {

    @NotNull
    private IngredientDto ingredient;

    @NotNull
    private float amount;
}
