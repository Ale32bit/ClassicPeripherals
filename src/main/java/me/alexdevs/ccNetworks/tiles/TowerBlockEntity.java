package me.alexdevs.ccNetworks.tiles;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.block.tower.TowerHeadBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TowerBlockEntity extends AbstractRadioBlockEntity {

    public TowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockTiles.TOWER_BASE, pos, state);
    }

    public void validate() {
        towerHeight = 1;
        isValid = false;
        var pos = this.getBlockPos();
        for (int i = 1; i < MAX_HEIGHT - 1; i++) {
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

        if (towerHeight < MIN_HEIGHT) {
            invalidate();
            return;
        }

        this.topPos = pos.immutable();
        super.validate();
    }

    @Override
    public void ping() {
        var head = this.level.getBlockState(getTopPos());
        this.level.setBlockAndUpdate(getTopPos(), head.setValue(TowerHeadBlock.ACTIVE, true));
        level.scheduleTick(getTopPos(), ModBlocks.TOWER_HEAD, 4);
    }
}
