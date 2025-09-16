package ch.schuum.backend.mapping;

import ch.schuum.backend.dto.IngredientDto;
import ch.schuum.backend.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Der Ingredient ist zuständig für das Mapping zwischen einem Ingredient-
 * Model und einem Ingredient-Dto, und umgekehrt.
 *
 * @author Miriam Streit
 *
 */
@Mapper
public interface IngredientMapper {
    IngredientMapper INSTANCE = Mappers.getMapper( IngredientMapper.class );

    /**
     * <p>konvertiert ein Data Transfer Object in ein Model Objekt</p>
     * @param dto das zu konvertierende DTO
     * @return das aus dem DTO erzeugte Model
     * @author Miriam Streit
     */
    Ingredient dtoToModel(IngredientDto dto);

    /**
     * <p>konvertiert ein Model Objekt in ein Data Transfer Object</p>
     * @param model das zu konvertierende Model
     * @return das aus dem Model erzeugte DTO
     * @author Miriam Streit
     */
    IngredientDto modelToDto(Ingredient model);

    /**
     * <p>konvertiert eine Liste von Data Transfer Objects in eine Liste von Model Objekten</p>
     * @param dtoList die zu konvertierende DTO Liste
     * @return die aus der DTO Liste erzeugte Model Liste
     * @author Miriam Streit
     */
    List<Ingredient> dtoListToModelList(List<IngredientDto> dtoList);

    /**
     * <p>konvertiert eine Liste von Models in eine Liste von Data Transfer Objects</p>
     * @param modelList die zu konvertierende Model Liste
     * @return die aus der Model Liste erzeugte DTO Liste
     * @author Miriam Streit
     */
    List<IngredientDto> modelListToDtoList(List<Ingredient> modelList);
}
