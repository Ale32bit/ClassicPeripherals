package me.alexdevs.classicPeripherals.block;

import me.alexdevs.classicPeripherals.tiles.CryptographicAcceleratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CryptographicAcceleratorBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public CryptographicAcceleratorBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CryptographicAcceleratorBlockEntity(pos, state);
    }
}
