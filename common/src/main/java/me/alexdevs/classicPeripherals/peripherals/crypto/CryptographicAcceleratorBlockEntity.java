package me.alexdevs.classicPeripherals.peripherals.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ModRegistry;
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
        super(ModRegistry.TileEntities.CRYPTOGRAPHIC_ACCELERATOR.get(), pos, blockState);
    }

    public AbstractCryptographicAcceleratorPeripheral peripheral() {
        return peripheral;
    }
}
