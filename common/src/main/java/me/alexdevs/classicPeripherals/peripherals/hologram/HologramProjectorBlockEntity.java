package me.alexdevs.classicPeripherals.peripherals.hologram;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HologramProjectorBlockEntity extends BlockEntity {
    private final HologramProjectorPeripheral peripheral = new HologramProjectorPeripheral(this);

    public HologramProjectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.HOLOGRAM_PROJECTOR.get(), pos, blockState);
    }

    public HologramProjectorPeripheral peripheral() {
        return peripheral;
    }
}
