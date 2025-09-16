package ch.schuum.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Die Klasse BrewingProcess beschreibt alle nötigen Attribute eines Brauprozesses.
 * Ebenfalls wird hier die Datenbank-Entität des Brauprozesses beschrieben.
 * Die Getter, Setter und Konstruktoren sind durch Lombok implementiert.
 *
 * Model für {@link ch.schuum.backend.dto.BrewingProcessDto}
 *
 * @author Miriam Streit
 *
 */
@Entity(name = "BrewingProcess")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrewingProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int brewingId;

    @ManyToOne
    @JoinColumn(name = "beer_type_id")
    private BeerType beerType;

    @ManyToOne
    @JoinColumn(name = "tank_id")
    private Tank tank;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    private String sudNumber;

    private String comment;

    private String color;

    private boolean deleted;
}
