package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.peripherals.AbstractCryptographicAcceleratorPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CryptographicAcceleratorBlockEntity extends BlockEntity {
    public static class CryptographicAcceleratorPeripheral extends AbstractCryptographicAcceleratorPeripheral {
        private final CryptographicAcceleratorBlockEntity cryptographicAccelerator;

        public CryptographicAcceleratorPeripheral(CryptographicAcceleratorBlockEntity blockEntity) {
            this.cryptographicAccelerator = blockEntity;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return other instanceof CryptographicAcceleratorPeripheral o && cryptographicAccelerator == o.cryptographicAccelerator;
        }
    }

    private final CryptographicAcceleratorPeripheral peripheral = new CryptographicAcceleratorPeripheral(this);

    public CryptographicAcceleratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.CRYPTOGRAPHIC_ACCELERATOR, pos, blockState);
    }

    public AbstractCryptographicAcceleratorPeripheral peripheral() {
        return peripheral;
    }
}
