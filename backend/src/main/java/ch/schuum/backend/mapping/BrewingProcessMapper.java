package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.BrewingProcessDto;
import ch.schuum.backend.model.BrewingProcess;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Der BrewingProcessMapper ist zuständig für das Mapping zwischen einem Brauprozess-
 * Model und einem Brauprozess-Dto, und umgekehrt.
 *
 * abhängig von {@link ch.schuum.backend.mapping.BeerTypeMapper}, {@link ch.schuum.backend.mapping.TankMapper}
 *
 * @author Miriam Streit
 *
 */
@Mapper(uses = {BeerTypeMapper.class, TankMapper.class})
public interface BrewingProcessMapper {
    BrewingProcessMapper INSTANCE = Mappers.getMapper( BrewingProcessMapper.class );

    /**
     * <p>konvertiert ein Data Transfer Object in ein Model Objekt</p>
     * @param dto das zu konvertierende DTO
     * @return das aus dem DTO erzeugte Model
     * @author Miriam Streit
     */
    BrewingProcess dtoToModel(BrewingProcessDto dto);

    /**
     * <p>konvertiert ein Model Objekt in ein Data Transfer Object</p>
     * @param model das zu konvertierende Model
     * @return das aus dem Model erzeugte DTO
     * @author Miriam Streit
     */
    BrewingProcessDto modelToDto(BrewingProcess model);

    /**
     * <p>konvertiert eine Liste von Data Transfer Objects in eine Liste von Model Objekten</p>
     * @param dtoList die zu konvertierende DTO Liste
     * @return die aus der DTO Liste erzeugte Model Liste
     * @author Miriam Streit
     */
    List<BrewingProcess> dtoListToModelList(List<BrewingProcessDto> dtoList);

    /**
     * <p>konvertiert eine Liste von Models in eine Liste von Data Transfer Objects</p>
     * @param modelList die zu konvertierende Model Liste
     * @return die aus der Model Liste erzeugte DTO Liste
     * @author Miriam Streit
     */
    List<BrewingProcessDto> modelListToDtoList(List<BrewingProcess> modelList);
}
