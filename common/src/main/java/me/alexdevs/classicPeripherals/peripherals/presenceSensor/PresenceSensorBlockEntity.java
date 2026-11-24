package me.alexdevs.classicPeripherals.peripherals.presenceSensor;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PresenceSensorBlockEntity extends BlockEntity {

    protected final PresenceSensorPeripheral peripheral = new PresenceSensorPeripheral(this);

    public PresenceSensorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.PRESENCE_SENSOR.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PresenceSensorBlockEntity scanner) {
        scanner.peripheral.tick(level);
    }

    @Nullable
    public PresenceSensorPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }
}
