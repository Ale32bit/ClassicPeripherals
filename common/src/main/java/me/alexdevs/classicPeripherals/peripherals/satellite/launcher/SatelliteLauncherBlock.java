package me.alexdevs.classicPeripherals.peripherals.satellite.launcher;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SatelliteLauncherBlock extends Block implements EntityBlock {
    public SatelliteLauncherBlock(Properties properties) {
        super(properties);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new SatelliteLauncherBlockEntity(blockPos, blockState);
    }
}
