package me.alexdevs.classicPeripherals.block;

import com.mojang.serialization.MapCodec;
import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import me.alexdevs.classicPeripherals.tiles.RfidScannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RfidScannerBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected RfidScannerBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(ACTIVE, false)
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return simpleCodec(RfidScannerBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ModemShapes.getBounds(state.getValue(FACING));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var facing = state.getValue(FACING);
        return ModemShapes.canSupport(level, pos.relative(facing), facing.getOpposite());
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    @Deprecated
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RfidScannerBlockEntity(pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));

        var be = level.getBlockEntity(pos);
        if(be instanceof RfidScannerBlockEntity scanner) {
            if(!level.isClientSide) {
                scanner.scan();
            }
        }
    }
}
