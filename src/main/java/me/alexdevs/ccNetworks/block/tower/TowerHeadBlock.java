package me.alexdevs.ccNetworks.block.tower;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.tiles.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TowerHeadBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public TowerHeadBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
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
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }
}
