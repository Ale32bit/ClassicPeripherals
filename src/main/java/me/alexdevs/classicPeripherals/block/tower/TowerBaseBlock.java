package me.alexdevs.classicPeripherals.block.tower;

import me.alexdevs.classicPeripherals.block.AbstractRadioBlock;
import me.alexdevs.classicPeripherals.tiles.AbstractRadioBlockEntity;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import me.alexdevs.classicPeripherals.tiles.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class TowerBaseBlock extends AbstractRadioBlock {
    public TowerBaseBlock(Properties settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TowerBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.or(
                Shapes.box(0, 0, 0, 16 / 16d, 1 / 16d, 16 / 16d),
                Shapes.box(1 / 16d, 1 / 16d, 1 / 16d, 15 / 16d, 16 / 16d, 15 / 16d)
        );
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModBlockTiles.TOWER_BASE, AbstractRadioBlockEntity::tick);
    }
}
