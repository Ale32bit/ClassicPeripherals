package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.tower.TowerHeadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AntennaBlockEntity extends AbstractRadioBlockEntity {

    public AntennaBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.ANTENNA, pos, blockState);
    }

    @Override
    public BlockPos getTopPos() {
        return this.getBlockPos();
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public boolean canBroadcast() {
        return false;
    }

    @Override
    public void ping() {
        var block = getBlockState();
        this.level.setBlockAndUpdate(getBlockPos(), block.setValue(TowerHeadBlock.ACTIVE, true));
        level.scheduleTick(getBlockPos(), ModBlocks.ANTENNA, 4);
    }
}
