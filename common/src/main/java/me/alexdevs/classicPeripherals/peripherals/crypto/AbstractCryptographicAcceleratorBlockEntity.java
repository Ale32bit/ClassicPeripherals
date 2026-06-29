package me.alexdevs.classicPeripherals.peripherals.crypto;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public abstract class AbstractCryptographicAcceleratorBlockEntity extends BlockEntity {
    public static class CryptographicAcceleratorPeripheral extends AbstractCryptographicAcceleratorPeripheral {
        protected final AbstractCryptographicAcceleratorBlockEntity cryptographicAccelerator;

        public CryptographicAcceleratorPeripheral(AbstractCryptographicAcceleratorBlockEntity blockEntity) {
            this.cryptographicAccelerator = blockEntity;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return other instanceof CryptographicAcceleratorPeripheral o && cryptographicAccelerator == o.cryptographicAccelerator;
        }
    }

    protected final CryptographicAcceleratorPeripheral peripheral = new CryptographicAcceleratorPeripheral(this);

    public AbstractCryptographicAcceleratorBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState blockState) {
        super(entityType, pos, blockState);
    }
}
