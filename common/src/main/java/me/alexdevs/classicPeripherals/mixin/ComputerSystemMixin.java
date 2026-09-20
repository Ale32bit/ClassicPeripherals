package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.shared.computer.core.ServerComputer;
import me.alexdevs.classicPeripherals.mixinInterface.IComputerSystemMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "dan200.computercraft.shared.computer.core.ComputerSystem", remap = false)
public abstract class ComputerSystemMixin implements IComputerSystemMixin {
    @Shadow
    @Final
    private ServerComputer computer;

    public ServerComputer classicPeripheral$getComputer() {
        return computer;
    }
}
