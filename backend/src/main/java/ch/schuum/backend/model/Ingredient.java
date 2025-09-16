package ch.schuum.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Die Klasse Ingredient beschreibt alle nötigen Attribute einer Zutat.
 * Ebenfalls wird hier die Datenbank-Entität der Zutat beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.IngredientDto}
 *
 * @author Miriam Streit
 *
 */
@Entity(name = "Ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ingredientId;

    private String name;

    private float stock;

    private float threshold;

    private boolean deleted;
}
