package me.alexdevs.ccNetworks.tiles;

import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.core.Tower;
import me.alexdevs.ccNetworks.core.TowerNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class TowerBaseBlockEntity extends BlockEntity {
    public static final int MAX_HEIGHT = 32;
    public static final int MIN_HEIGHT = 4;
    public static final int SEGMENT_RANGE = 128;

    private int towerHeight = 1;
    private boolean isValid = false;

    private Tower tower;

    public TowerBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockTiles.TOWER_BASE, pos, state);
    }

    public void calculateTower() {
        towerHeight = 1;
        isValid = false;
        var pos = this.getBlockPos();
        for(int i = 1; i < MAX_HEIGHT - 1; i++) {
            pos = pos.above(1);
            if(this.level.getBlockState(pos).is(ModBlocks.TOWER_SEGMENT)) {
                towerHeight++;
            } else {
                break;
            }
        }

        if(!this.level.getBlockState(pos).is(ModBlocks.TOWER_HEAD)) {
            invalidate();
            return;
        }
        towerHeight++;

        if(towerHeight < MIN_HEIGHT) {
            invalidate();
            return;
        }

        isValid = true;

        tower = new Tower(this);
        TowerNetwork.addTower(tower);
    }

    public void invalidate() {
        if(tower != null) {
            TowerNetwork.removeTower(tower);
            tower = null;
        }
    }

    public @Nullable Tower getTower() {
        return tower;
    }

    public int getTowerHeight() {
        return towerHeight;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getRange() {
        if(!isValid)
            return 0;

        return SEGMENT_RANGE * towerHeight;
    }
}
