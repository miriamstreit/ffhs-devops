package ch.schuum.backend.service;

import ch.schuum.backend.dto.TankDto;
import ch.schuum.backend.mapping.BrewingProcessMapper;
import ch.schuum.backend.mapping.TankMapper;
import ch.schuum.backend.model.BrewingProcess;
import ch.schuum.backend.model.Tank;
import ch.schuum.backend.repository.TankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Der TankService ist zuständig für die Business Logik zur Domäne "Tanks"
 * und verbindet die Schichten Controller und Repository
 *
 * @author Miriam Streit
 *
 */
@RequiredArgsConstructor
@Service
public class TankService {
    private final TankRepository tankRepository;
    private final BrewingProcessService brewingProcessService;

    /**
     * <p>holt alle Tanks</p>
     * @return alle Tanks
     * @author Florian Susuri
     */
    public List<TankDto> getAllTanks() {
        List<TankDto> tanks = TankMapper.INSTANCE.modelListToDtoList(tankRepository.findAll());
        return tanks.stream().map(tank -> {
            BrewingProcess currentBrewingProcess = brewingProcessService.getCurrentBrewingProcessByTankId(tank.getTankId());
            if (currentBrewingProcess != null) {
                tank.setBrewingProcess(BrewingProcessMapper.INSTANCE.modelToDto(currentBrewingProcess));
            }
            return tank;
        }).collect(Collectors.toList());
    }

    /**
     * <p>holt einen Tank mit seiner ID</p>
     * @param id ID des gesuchten Tanks
     * @return den gesuchten Tank als Optional oder leerer Optional
     * @author Florian Susuri
     */
    public Optional<TankDto> getTankById(int id) {
        Optional<Tank> tankModel = tankRepository.findById(id);
        if (tankModel.isPresent()) {
            TankDto tank = TankMapper.INSTANCE.modelToDto(tankModel.get());
            BrewingProcess currentBrewingProcess = brewingProcessService.getCurrentBrewingProcessByTankId(tank.getTankId());
            if (currentBrewingProcess != null) {
                tank.setBrewingProcess(BrewingProcessMapper.INSTANCE.modelToDto(currentBrewingProcess));
            }
            return Optional.of(tank);
        }
        return Optional.empty();
    }

    /**
     * <p>speichert einen neuen Tank</p>
     * @param tank der neue, zu speichernde Tank
     * @return der gespeicherte Tank
     * @author Florian Susuri
     */
    public TankDto createTank(TankDto tank) {
        Tank tankModel = TankMapper.INSTANCE.dtoToModel(tank);
        Tank savedObject = tankRepository.save(tankModel);
        return TankMapper.INSTANCE.modelToDto(savedObject);
    }

    /**
     * <p>aktualisiert einen bestehenden Tank</p>
     * <p>wenn der Tank nicht existiert, wird er stattdessen als neuer gespeichert</p>
     * @param id die ID des zu aktualisierenden Tanks
     * @param tank der aktualisierte Tank
     * @return der aktualisierte Tank
     * @author Florian Susuri
     */
    public TankDto updateTank(int id, TankDto tank) {
        Optional<Tank> existingTank = tankRepository.findById(id);
        if (existingTank.isPresent()) {
            existingTank.get().setVolume(tank.getVolume());
            return TankMapper.INSTANCE.modelToDto(tankRepository.save(existingTank.get()));
        }
        return createTank(tank);
    }
}
