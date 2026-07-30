package me.alexdevs.classicPeripherals.peripherals.radio;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.RadioNetwork;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

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
            return Vec3.atLowerCornerOf(be.getAntennaBlockPos());
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

    public static final double RANGE_COEFFICIENT = 1.0375;

    protected int towerHeight = 1;
    protected boolean isValid = true;
    protected final RadioPeripheral peripheral = new RadioPeripheral(this);
    protected boolean initialized = false;
    protected int pingTicks = 4;
    protected long lastPing = 0;

    protected boolean shouldClearPing = false;

    public AbstractRadioBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }


    @Override
    protected void loadAdditional(@NonNull CompoundTag nbt, HolderLookup.@NonNull Provider registries) {
        super.loadAdditional(nbt, registries);

        if (nbt.contains("radio_channel")) {
            peripheral.setChannel(nbt.getInt("radio_channel"));
        }
    }

    @Override
    protected void saveAdditional(@NonNull CompoundTag nbt, HolderLookup.@NonNull Provider registries) {
        super.saveAdditional(nbt, registries);

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
                be.shouldClearPing = true;
            } else if (delta >= be.pingTicks && be.shouldClearPing) {
                be.afterPing();
                be.shouldClearPing = false;
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
        var radioNetwork = ClassicPeripherals.getRadioNetwork();
        Objects.requireNonNull(radioNetwork);
        radioNetwork.addReceiver(peripheral);
    }

    public void invalidate() {
        isValid = false;
        var radioNetwork = ClassicPeripherals.getRadioNetwork();
        Objects.requireNonNull(radioNetwork);
        radioNetwork.removeReceiver(peripheral);
    }

    public void ping() {
        if (level != null) {
            lastPing = level.getGameTime();
        }
    }

    protected abstract void onPing();

    protected abstract void afterPing();

    /**
     * Get the position of the antenna block.
     * @return Physical position of the antenna block
     */
    public abstract BlockPos getAntennaBlockPos();

    /**
     * Get the position of the antenna used for radio communication.
     * @return Get the logical position of the antenna block.
     */
    public Vec3 getAntennaRadioPos() {
        var vec = getAntennaBlockPos().getCenter();
        return SableIntegration.getTranslatedPos(this.getLevel(), vec);
    }

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
        var y = this.getAntennaRadioPos().y();

        var maxRange = getMaximumRange();

        if (y >= ClassicPeripherals.CONFIG.radioTowerMinY) {
            return maxRange;
        }

        return (int)(maxRange * Math.pow(RANGE_COEFFICIENT, (y - ClassicPeripherals.CONFIG.radioTowerMinY)));
    }
}
