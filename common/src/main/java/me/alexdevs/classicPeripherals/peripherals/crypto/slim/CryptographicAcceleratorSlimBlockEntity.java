package me.alexdevs.classicPeripherals.peripherals.crypto.slim;

import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.crypto.AbstractCryptographicAcceleratorBlockEntity;
import me.alexdevs.classicPeripherals.peripherals.crypto.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CryptographicAcceleratorSlimBlockEntity extends AbstractCryptographicAcceleratorBlockEntity {
    public CryptographicAcceleratorSlimBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.CRYPTOGRAPHIC_ACCELERATOR_SLIM.get(), pos, blockState);
    }

    public AbstractCryptographicAcceleratorPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }
}
