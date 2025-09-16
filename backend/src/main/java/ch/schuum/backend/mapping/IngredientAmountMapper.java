package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.IngredientAmountDto;
import ch.schuum.backend.model.IngredientAmount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Der IngredientAmountMapper ist zuständig für das Mapping zwischen einem
 * IngredientAmount-Model und einem IngredientAmount-Dto, und umgekehrt.
 *
 * abhängig von {@link ch.schuum.backend.mapping.IngredientMapper}
 *
 * @author Miriam Streit
 *
 */
@Mapper(uses = IngredientMapper.class)
public interface IngredientAmountMapper {
    IngredientAmountMapper INSTANCE = Mappers.getMapper( IngredientAmountMapper.class );

    /**
     * <p>konvertiert ein Data Transfer Object in ein Model Objekt</p>
     * @param dto das zu konvertierende DTO
     * @return das aus dem DTO erzeugte Model
     * @author Miriam Streit
     */
    IngredientAmount dtoToModel(IngredientAmountDto dto);

    /**
     * <p>konvertiert ein Model Objekt in ein Data Transfer Object</p>
     * @param model das zu konvertierende Model
     * @return das aus dem Model erzeugte DTO
     * @author Miriam Streit
     */
    IngredientAmountDto modelToDto(IngredientAmount model);

    /**
     * <p>konvertiert eine Liste von Data Transfer Objects in eine Liste von Model Objekten</p>
     * @param dtoList die zu konvertierende DTO Liste
     * @return die aus der DTO Liste erzeugte Model Liste
     * @author Miriam Streit
     */
    List<IngredientAmount> dtoListToModelList(List<IngredientAmountDto> dtoList);

    /**
     * <p>konvertiert eine Liste von Models in eine Liste von Data Transfer Objects</p>
     * @param modelList die zu konvertierende Model Liste
     * @return die aus der Model Liste erzeugte DTO Liste
     * @author Miriam Streit
     */
    List<IngredientAmountDto> modelListToDtoList(List<IngredientAmount> modelList);
}
