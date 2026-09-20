package me.alexdevs.classicPeripherals.integrations;

import dan200.computercraft.api.lua.IComputerSystem;
import io.sc3.plethora.gameplay.neural.NeuralComputer;
import io.sc3.plethora.gameplay.neural.NeuralPocketAccess;
import me.alexdevs.classicPeripherals.mixinInterface.IComputerSystemMixin;
import me.alexdevs.classicPeripherals.platform.services.IPlethoraIntegration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

// Re:Plethora
public class FabricPlethoraIntegration implements IPlethoraIntegration {
    @Override
    public boolean isNeural(IComputerSystem system) {
        return getNeuralComputer(system).isPresent();
    }

    @Override
    public Vec3 getPosition(IComputerSystem system) {
        return getAccess(system).map(NeuralPocketAccess::getPosition).orElse(null);
    }

    @Override
    public Entity getEntity(IComputerSystem system) {
        return getAccess(system).map(NeuralPocketAccess::getEntity).orElse(null);
    }

    private Optional<NeuralComputer> getNeuralComputer(IComputerSystem system) {
        var computer = ((IComputerSystemMixin) system).classicPeripheral$getComputer();
        if (computer instanceof NeuralComputer neuralComputer) {
            return Optional.of(neuralComputer);
        }
        return Optional.empty();
    }

    private Optional<NeuralPocketAccess> getAccess(IComputerSystem system) {
        var neural = getNeuralComputer(system);
        return neural.map(NeuralPocketAccess::new);
    }
}
