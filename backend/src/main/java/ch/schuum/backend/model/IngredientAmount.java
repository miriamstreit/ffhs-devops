package ch.schuum.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Die Klasse IngredientAmount umfasst eine Zutat und die zugehörige Menge,
 * die im Rezept einer Biersorte benötigt werden.
 * In der Datenbank umgesetzt wird die Klasse als Embeddable von {@link ch.schuum.backend.model.BeerType}.
 * Ebenfalls werden hier die Verbindung zwischen der Klasse und zwischen der Zutat beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.IngredientAmountDto}
 *
 * @author Miriam Streit
 *
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientAmount {

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private float amount;
}
