package ch.schuum.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Die Klasse BeerType beschreibt alle nötigen Attribute einer Biersorte.
 * Ebenfalls wird hier die Datenbank-Entität der Biersorte beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.BeerTypeDto}
 *
 * @author Miriam Streit
 *
 */
@Entity(name = "BeerType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeerType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int beerTypeId;

    private String name;

    private int duration;

    @ElementCollection
    @JsonIgnoreProperties("beerType")
    private List<IngredientAmount> ingredients;

    private boolean deleted;
}
