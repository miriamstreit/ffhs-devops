package ch.schuum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse BrewableDto beschreibt, ob eine Biersorte mit ihren Zutaten gebraut werden kann,
 * oder welche Zutaten noch fehlen. Da diese Daten bereits in anderer Form in der Datenbank stehen
 * und das Objekt nur dem Datentransfer dient, existiert kein Model dazu.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrewableDto {
    private String name;

    private boolean isBrewable;

    private List<IngredientAmountDto> missingIngredients = new ArrayList<>();
}
