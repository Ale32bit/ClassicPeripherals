package me.alexdevs.classicPeripherals.peripherals.crypto.full;

import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.crypto.AbstractCryptographicAcceleratorBlockEntity;
import me.alexdevs.classicPeripherals.peripherals.crypto.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CryptographicAcceleratorBlockEntity extends AbstractCryptographicAcceleratorBlockEntity {
    public CryptographicAcceleratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.CRYPTOGRAPHIC_ACCELERATOR.get(), pos, blockState);
    }

    public AbstractCryptographicAcceleratorPeripheral peripheral() {
        return peripheral;
    }
}
