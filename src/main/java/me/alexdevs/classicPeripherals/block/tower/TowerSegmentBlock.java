package me.alexdevs.classicPeripherals.block.tower;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.tiles.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
                    base.validate();
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.25d, 0, 0.25d, 0.75d, 1d, 0.75d);
    }
}
