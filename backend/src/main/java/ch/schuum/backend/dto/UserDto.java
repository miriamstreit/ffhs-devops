package ch.schuum.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Die Klasse UserDto beschreibt alle nötigen Attribute eines
 * Benutzers als Data Transfer Object. Ebenfalls wird die Validierung der
 * Klasse beschrieben, welche vom Controller verwendet wird und Fehlermeldungen
 * erzeugen kann.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * DTO für {@link ch.schuum.backend.model.User}
 *
 * @author Miriam Streit
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private int userId;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private String token;
}
