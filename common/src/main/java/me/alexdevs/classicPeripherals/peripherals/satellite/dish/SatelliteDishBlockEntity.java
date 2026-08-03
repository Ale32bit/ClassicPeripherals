package me.alexdevs.classicPeripherals.peripherals.satellite.dish;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SatelliteDishBlockEntity extends BlockEntity {

    protected final SatelliteDishPeripheral peripheral = new SatelliteDishPeripheral(this);

    public SatelliteDishBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.SATELLITE_DISH.get(), pos, blockState);
    }

    @Nullable
    public SatelliteDishPeripheral peripheral(@Nullable Direction direction) {
        return direction == null || getDirection() == direction ? peripheral : null;
    }

    public Direction getDirection() {
        return getBlockState().getValue(DirectionalBlock.FACING);
    }
}
