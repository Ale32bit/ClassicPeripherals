package me.alexdevs.classicPeripherals.peripherals.rfid;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RfidScannerBlockEntity extends BlockEntity {

    protected final RfidScannerPeripheral peripheral = new RfidScannerPeripheral(this);

    public RfidScannerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.RFID_SCANNER.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RfidScannerBlockEntity scanner) {
        scanner.peripheral.tick(level);
    }

    @Nullable
    public RfidScannerPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }
}
