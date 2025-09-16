package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.model.Tank;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Der TankMapper ist zuständig für das Mapping zwischen einem Tank-
 * Model und einem Tank-Dto, und umgekehrt.
 *
 * @author Miriam Streit
 *
 */
@Mapper
public interface TankMapper {
    TankMapper INSTANCE = Mappers.getMapper( TankMapper.class );

    /**
     * <p>konvertiert ein Data Transfer Object in ein Model Objekt</p>
     * @param dto das zu konvertierende DTO
     * @return das aus dem DTO erzeugte Model
     * @author Miriam Streit
     */
    Tank dtoToModel(TankDto dto);

    /**
     * <p>konvertiert ein Model Objekt in ein Data Transfer Object</p>
     * @param model das zu konvertierende Model
     * @return das aus dem Model erzeugte DTO
     * @author Miriam Streit
     */
    TankDto modelToDto(Tank model);

    /**
     * <p>konvertiert eine Liste von Data Transfer Objects in eine Liste von Model Objekten</p>
     * @param dtoList die zu konvertierende DTO Liste
     * @return die aus der DTO Liste erzeugte Model Liste
     * @author Miriam Streit
     */
    List<Tank> dtoListToModelList(List<TankDto> dtoList);

    /**
     * <p>konvertiert eine Liste von Models in eine Liste von Data Transfer Objects</p>
     * @param modelList die zu konvertierende Model Liste
     * @return die aus der Model Liste erzeugte DTO Liste
     * @author Miriam Streit
     */
    List<TankDto> modelListToDtoList(List<Tank> modelList);
}
