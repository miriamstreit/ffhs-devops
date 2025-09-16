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
 * Die Klasse Tank beschreibt alle nötigen Attribute eines Tanks.
 * Ebenfalls wird hier die Datenbank-Entität des Tanks beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.TankDto}
 *
 * @author Miriam Streit
 *
 */
@Entity(name = "Tank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tankId;

    private int volume;
}
