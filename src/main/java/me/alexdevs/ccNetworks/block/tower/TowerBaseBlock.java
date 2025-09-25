package me.alexdevs.ccNetworks.block.tower;

import me.alexdevs.ccNetworks.tiles.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class TowerBaseBlock extends BaseEntityBlock {
    public TowerBaseBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        var be = level.getBlockEntity(blockPos);
        if (be instanceof TowerBlockEntity base) {
            base.calculateTower();
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        var be = level.getBlockEntity(blockPos);
        if (be instanceof TowerBlockEntity base) {
            base.invalidate();
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TowerBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(
                Shapes.box(0, 0, 0, 16 / 16d, 1 / 16d, 16 / 16d),
                Shapes.box(1 / 16d, 1 / 16d, 1 / 16d, 15 / 16d, 16 / 16d, 15 / 16d)
        );
    }
}
