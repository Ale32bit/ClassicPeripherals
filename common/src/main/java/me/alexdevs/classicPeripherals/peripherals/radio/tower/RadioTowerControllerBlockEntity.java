package me.alexdevs.classicPeripherals.peripherals.radio.tower;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.radio.AbstractRadioBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class RadioTowerControllerBlockEntity extends AbstractRadioBlockEntity {

    protected BlockPos headPos;

    public RadioTowerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistry.TileEntities.TOWER_BASE.get(), pos, state);
    }

    public void validate() {
        towerHeight = 1;
        isValid = false;
        var pos = this.getBlockPos();
        for (int i = 1; i < ClassicPeripherals.CONFIG.radioTowerMaxHeight; i++) {
            pos = pos.above(1);
            if (this.level.getBlockState(pos).is(ModRegistry.Blocks.TOWER_SEGMENT.get())) {
                towerHeight++;
            } else {
                break;
            }
        }

        if (!this.level.getBlockState(pos).is(ModRegistry.Blocks.TOWER_HEAD.get())) {
            invalidate();
            return;
        }
        towerHeight++;

        if (towerHeight < ClassicPeripherals.CONFIG.radioTowerMinHeight) {
            invalidate();
            return;
        }

        headPos = pos.immutable();
        spawnParticles();
        super.validate();
    }

    private void spawnParticles() {
        var level = getLevel();
        if (level == null || level.isClientSide) {
            return;
        }

        var rng = level.getRandom();
        var startPos = getBlockPos().mutable();
        for (int i = 0; i < towerHeight; i++) {
            for (int j = 0; j < 5; j++) {
                ((ServerLevel) level).sendParticles(DustParticleOptions.REDSTONE, startPos.getX() + rng.nextFloat(), startPos.getY() + rng.nextFloat(), startPos.getZ() + rng.nextFloat(), 1, 0, 0, 0, 0);
            }

            startPos.move(Direction.UP);
        }
    }

    @Override
    protected void onPing() {
        if (level != null) {
            var head = level.getBlockState(getAntennaPos());
            if(head.is(ModRegistry.Blocks.TOWER_HEAD.get())) {
                this.level.setBlockAndUpdate(getAntennaPos(), head.setValue(RadioTowerAntennaBlock.ACTIVE, true));
            }
        }
    }

    @Override
    protected void afterPing() {
        if (level != null) {
            var head = level.getBlockState(getAntennaPos());
            if(head.is(ModRegistry.Blocks.TOWER_HEAD.get())) {
                this.level.setBlockAndUpdate(getAntennaPos(), head.setValue(RadioTowerAntennaBlock.ACTIVE, false));
            }
        }
    }

    @Override
    public BlockPos getAntennaPos() {
        return getBlockPos().above(towerHeight - 1);
    }
}
