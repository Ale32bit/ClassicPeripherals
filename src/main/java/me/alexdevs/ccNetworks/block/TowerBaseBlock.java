package me.alexdevs.ccNetworks.block;

import me.alexdevs.ccNetworks.tiles.TowerBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class TowerBaseBlock extends BaseEntityBlock {
    public TowerBaseBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);

        var be = level.getBlockEntity(blockPos);
        if(be instanceof TowerBaseBlockEntity base) {
            base.calculateTower();
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TowerBaseBlockEntity(blockPos, blockState);
    }
}
