package me.alexdevs.classicPeripherals.peripherals.radio.antenna;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.radio.AbstractRadioBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RadioAntennaBlockEntity extends AbstractRadioBlockEntity {

    public RadioAntennaBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModRegistry.TileEntities.ANTENNA.get(), pos, blockState);
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
        this.level.setBlockAndUpdate(getBlockPos(), block.setValue(RadioAntennaBlock.ACTIVE, true));
    }

    @Override
    protected void afterPing() {
        var block = getBlockState();
        this.level.setBlockAndUpdate(getBlockPos(), block.setValue(RadioAntennaBlock.ACTIVE, false));
    }
}
