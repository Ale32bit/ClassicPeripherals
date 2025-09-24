package me.alexdevs.ccNetworks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AntennaBlockEntity extends BlockEntity {
    private int channel = 0;

    public AntennaBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.ANTENNA, pos, blockState);
    }
}
