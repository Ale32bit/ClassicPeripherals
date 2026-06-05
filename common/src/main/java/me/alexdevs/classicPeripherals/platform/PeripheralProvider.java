package me.alexdevs.classicPeripherals.platform;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/** Supplies a peripheral for a block entity on a given side, or {@code null} if none is exposed. */
@FunctionalInterface
public interface PeripheralProvider<B extends BlockEntity> {
    @Nullable
    IPeripheral getPeripheral(B blockEntity, @Nullable Direction side);
}
