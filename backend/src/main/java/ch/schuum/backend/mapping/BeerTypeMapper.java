package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.BeerTypeDto;
import ch.schuum.backend.model.BeerType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Der BeerTypeMapper ist zuständig für das Mapping zwischen einem
 * BeerType-Model und einem BeerType-Dto, und umgekehrt.
 *
 * abhängig von {@link ch.schuum.backend.mapping.IngredientAmountMapper}, {@link ch.schuum.backend.mapping.IngredientMapper}
 *
 * @author Miriam Streit
 *
 */
@Mapper(uses = {IngredientAmountMapper.class, IngredientMapper.class})
public interface BeerTypeMapper {
    BeerTypeMapper INSTANCE = Mappers.getMapper( BeerTypeMapper.class );

    /**
     * <p>konvertiert ein Data Transfer Object in ein Model Objekt</p>
     * @param dto das zu konvertierende DTO
     * @return das aus dem DTO erzeugte Model
     * @author Miriam Streit
     */
    BeerType dtoToModel(BeerTypeDto dto);

    /**
     * <p>konvertiert ein Model Objekt in ein Data Transfer Object</p>
     * @param model das zu konvertierende Model
     * @return das aus dem Model erzeugte DTO
     * @author Miriam Streit
     */
    BeerTypeDto modelToDto(BeerType model);

    /**
     * <p>konvertiert eine Liste von Data Transfer Objects in eine Liste von Model Objekten</p>
     * @param dtoList die zu konvertierende DTO Liste
     * @return die aus der DTO Liste erzeugte Model Liste
     * @author Miriam Streit
     */
    List<BeerType> dtoListToModelList(List<BeerTypeDto> dtoList);

    /**
     * <p>konvertiert eine Liste von Models in eine Liste von Data Transfer Objects</p>
     * @param modelList die zu konvertierende Model Liste
     * @return die aus der Model Liste erzeugte DTO Liste
     * @author Miriam Streit
     */
    List<BeerTypeDto> modelListToDtoList(List<BeerType> modelList);
}
