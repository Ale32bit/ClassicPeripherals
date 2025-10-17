package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.TowerNetwork;
import me.alexdevs.classicPeripherals.peripherals.RadioPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class AbstractRadioBlockEntity extends BlockEntity {
    protected final Random random = new Random();

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
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);

        if (nbt.contains("radio_channel")) {
            peripheral.setChannel(nbt.getInt("radio_channel"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
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

    public boolean inRange(AbstractRadioBlockEntity other) {
        var range = Math.max(this.getMaximumRange(), other.getMaximumRange());
        var distance = getAntennaPos().atY(255).distSqr(other.getAntennaPos().atY(255));
        return distance <= range * range;
    }

    public String flipString(String data, double percentage) {
        var bytes = data.getBytes(StandardCharsets.US_ASCII);
        var total = bytes.length * 8;
        var toFlip = (int) Math.ceil(total * percentage);

        for (int i = 0; i < toFlip; i++) {
            var bit = random.nextInt(total);
            var byteIndex = bit / 8;
            var bitIndex = bit % 8;
            bytes[byteIndex] ^= (byte) (1 << bitIndex);
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }
}
