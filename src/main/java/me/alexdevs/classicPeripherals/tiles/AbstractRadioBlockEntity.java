package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.TowerNetwork;
import me.alexdevs.classicPeripherals.peripherals.AbstractRadioPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class AbstractRadioBlockEntity extends BlockEntity {
    public static class RadioPeripheral extends AbstractRadioPeripheral {
        private final AbstractRadioBlockEntity be;
        public RadioPeripheral(AbstractRadioBlockEntity be) {
            this.be = be;
        }

        @Override
        public boolean isValid() {
            return be.isValid();
        }

        @Override
        public Level getLevel() {
            return be.getLevel();
        }

        @Override
        public Vec3 getPosition() {
            return Vec3.atLowerCornerOf(be.getAntennaPos());
        }

        @Override
        public double getRange() {
            return be.getEffectiveMaxRange();
        }

        @Override
        public void ping() {
            be.ping();
        }

        @Override
        public boolean canBroadcast() {
            return be.canBroadcast();
        }

        @Override
        public int getHeight() {
            return be.getHeight();
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return this == other || (other instanceof RadioPeripheral o && be == o.be);
        }
    }

    protected int towerHeight = 1;
    protected boolean isValid = true;
    protected final RadioPeripheral peripheral = new RadioPeripheral(this);
    protected boolean initialized = false;
    protected int pingTicks = 4;
    protected long lastPing = 0;

    public AbstractRadioBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        if (nbt.contains("radio_channel")) {
            peripheral.setChannel(nbt.getInt("radio_channel"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.putInt("radio_channel", peripheral.getChannel());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        invalidate();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractRadioBlockEntity be) {
        if (!be.initialized) {
            be.initialized = true;
            be.validate();
        } else if (be.isValid) {
            // Skip the first ever tick
            var time = level.getGameTime();

            var delta = time - be.lastPing;
            if (delta == 0) {
                be.onPing();
            } else if (delta >= be.pingTicks) {
                be.afterPing();
            }
        }
    }

    public static double getSafeRange(double maxRange) {
        return maxRange - (maxRange * ClassicPeripherals.CONFIG.radioTowerLossFactor);
    }

    public boolean canBroadcast() {
        return isValid;
    }

    public void validate() {
        isValid = true;
        TowerNetwork.addReceiver(peripheral);
    }

    public void invalidate() {
        isValid = false;
        TowerNetwork.removeReceiver(peripheral);
    }

    public void ping() {
        if (level != null) {
            lastPing = level.getGameTime();
        }
    }

    protected abstract void onPing();

    protected abstract void afterPing();

    public abstract BlockPos getAntennaPos();

    public int getHeight() {
        return towerHeight;
    }

    public boolean isValid() {
        return isValid;
    }


    public IPeripheral peripheral() {
        return peripheral;
    }

    public int getMaximumRange() {
        if (!isValid)
            return 0;

        return towerHeight * ClassicPeripherals.CONFIG.radioTowerSegmentRange;
    }

    public int getEffectiveMaxRange() {
        var y = this.getAntennaPos().getY();

        var range = getMaximumRange();

        if (y >= 96) {
            return range;
        }

        return Math.max(8, (int) (96 * (1 - Math.pow(Math.E, -0.05 * y)) / 100d * range));
    }
}
