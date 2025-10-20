package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ClassicPeripheralsConfig;
import me.alexdevs.classicPeripherals.block.antenna.AntennaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AntennaBlockEntity extends AbstractRadioBlockEntity {

    public AntennaBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockTiles.ANTENNA.get(), pos, blockState);
    }

    @Override
    public BlockPos getAntennaPos() {
        return this.getBlockPos();
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getMaximumRange() {
        return 0;
    }

    @Override
    public int getEffectiveMaxRange() {
        return 0;
    }

    @Override
    public boolean canBroadcast() {
        return ClassicPeripherals.CONFIG.antennaCanBroadcast;
    }

    @Override
    protected void onPing() {
        var block = getBlockState();
        this.level.setBlockAndUpdate(getBlockPos(), block.setValue(AntennaBlock.ACTIVE, true));
    }

    @Override
    protected void afterPing() {
        var block = getBlockState();
        this.level.setBlockAndUpdate(getBlockPos(), block.setValue(AntennaBlock.ACTIVE, false));
    }
}
