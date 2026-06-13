package me.alexdevs.classicPeripherals.peripherals.radio.tower;

import me.alexdevs.classicPeripherals.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class RadioTowerPoleBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public RadioTowerPoleBlock(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState()
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public @NonNull BlockState updateShape(BlockState state, @NonNull Direction direction, @NonNull BlockState neighborState, @NonNull LevelAccessor level, @NonNull BlockPos pos, @NonNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    public @NonNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        var waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState()
                .setValue(WATERLOGGED, waterlogged);
    }

    private void triggerBase(Level level, BlockPos blockPos) {
        for (int i = 1; i < 32; i++) {
            BlockPos below = blockPos.below(i);
            var blockBelow = level.getBlockState(below);
            if (blockBelow.is(ModRegistry.Blocks.TOWER_BASE.get())) {
                var be = level.getBlockEntity(below);
                if (be instanceof RadioTowerControllerBlockEntity base)
                    base.validate();
                break;
            }
        }
    }

    @Override
    public void onPlace(@NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull BlockState oldState, boolean bl) {
        super.onPlace(blockState, level, blockPos, oldState, bl);

        if (!blockState.is(oldState.getBlock())) {
            triggerBase(level, blockPos);
        }
    }

    @Override
    public void onRemove(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull BlockState newState, boolean moved) {
        super.onRemove(state, level, blockPos, newState, moved);

        if (!state.is(newState.getBlock())) {
            triggerBase(level, blockPos);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return Shapes.box(0.25d, 0, 0.25d, 0.75d, 1d, 0.75d);
    }
}
