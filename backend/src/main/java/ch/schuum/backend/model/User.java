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
 * Die Klasse User beschreibt alle nötigen Attribute eines Benutzers.
 * Ebenfalls wird hier die Datenbank-Entität des Benutzers beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.UserDto}
 *
 * @author Miriam Streit
 *
 */
@Entity(name="BeerUser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String email;

    private String password;

    private String token;
}
