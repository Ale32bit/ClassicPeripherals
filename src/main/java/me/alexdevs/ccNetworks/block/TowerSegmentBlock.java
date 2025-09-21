package me.alexdevs.ccNetworks.block;

import me.alexdevs.ccNetworks.tiles.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TowerSegmentBlock extends Block {
    public TowerSegmentBlock(Properties settings) {
        super(settings);
    }

    private void triggerBase(Level level, BlockPos blockPos) {
        for(int i = 1; i < 32; i++) {
            BlockPos below = blockPos.below(i);
            var blockBelow = level.getBlockState(below);
            if(blockBelow.is(ModBlocks.TOWER_BASE)) {
                var be = level.getBlockEntity(below);
                if(be instanceof TowerBlockEntity base)
                    base.calculateTower();
                break;
            }
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        triggerBase(level, blockPos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean moved) {
        super.onRemove(state, level, blockPos, newState, moved);

        triggerBase(level, blockPos);
    }
}
