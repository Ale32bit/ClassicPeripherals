package me.alexdevs.classicPeripherals.tiles;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.block.tower.TowerHeadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.NoSuchElementException;

public class TowerBlockEntity extends AbstractRadioBlockEntity {

    protected BlockPos headPos;

    public TowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockTiles.TOWER_BASE.get(), pos, state);
    }

    public void validate() {
        towerHeight = 1;
        isValid = false;
        var pos = this.getBlockPos();
        for (int i = 1; i < ClassicPeripherals.CONFIG.radioTowerMaxHeight; i++) {
            pos = pos.above(1);
            if (this.level.getBlockState(pos).is(ModBlocks.TOWER_SEGMENT)) {
                towerHeight++;
            } else {
                break;
            }
        }

        if (!this.level.getBlockState(pos).is(ModBlocks.TOWER_HEAD)) {
            invalidate();
            return;
        }
        towerHeight++;

        if (towerHeight < ClassicPeripherals.CONFIG.radioTowerMinHeight) {
            invalidate();
            return;
        }

        headPos = pos.immutable();
        super.validate();
    }

    @Override
    public void ping() {
        try {
            var head = this.level.getBlockState(getAntennaPos());
            this.level.setBlockAndUpdate(getAntennaPos(), head.setValue(TowerHeadBlock.ACTIVE, true));
            level.scheduleTick(getAntennaPos(), ModBlocks.TOWER_HEAD.get(), 4);
        } catch (NoSuchElementException e) {
            // No op
        }
    }

    @Override
    public BlockPos getAntennaPos() {
        return headPos;
    }
}
