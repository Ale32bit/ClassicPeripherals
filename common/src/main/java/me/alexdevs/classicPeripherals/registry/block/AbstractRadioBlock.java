package me.alexdevs.classicPeripherals.registry.block;

import me.alexdevs.classicPeripherals.registry.tiles.AbstractRadioBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public abstract class AbstractRadioBlock extends BaseEntityBlock {
    protected AbstractRadioBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NonNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(@NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        var be = level.getBlockEntity(blockPos);
        if (be instanceof AbstractRadioBlockEntity base) {
            base.validate();
        }
    }

    @Override
    public void onRemove(@NonNull BlockState blockState, Level level, @NonNull BlockPos blockPos, @NonNull BlockState blockState2, boolean bl) {
        var be = level.getBlockEntity(blockPos);
        if (be instanceof AbstractRadioBlockEntity base) {
            base.invalidate();
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
}
