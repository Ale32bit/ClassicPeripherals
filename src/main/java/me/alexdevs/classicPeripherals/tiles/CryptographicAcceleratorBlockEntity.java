package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.peripherals.CryptographicAcceleratorPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CryptographicAcceleratorBlockEntity extends BlockEntity {

    private final CryptographicAcceleratorPeripheral peripheral = new CryptographicAcceleratorPeripheral(this);

    public CryptographicAcceleratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.CRYPTOGRAPHIC_ACCELERATOR.get(), pos, blockState);
    }

    public CryptographicAcceleratorPeripheral peripheral() {
        return peripheral;
    }
}
