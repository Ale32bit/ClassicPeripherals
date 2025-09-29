package me.alexdevs.ccNetworks.block;

import me.alexdevs.ccNetworks.tiles.AbstractRadioBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractRadioBlock extends BaseEntityBlock {
    protected AbstractRadioBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        var be = level.getBlockEntity(blockPos);
        if (be instanceof AbstractRadioBlockEntity base) {
            base.validate();
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        var be = level.getBlockEntity(blockPos);
        if (be instanceof AbstractRadioBlockEntity base) {
            base.invalidate();
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
}
