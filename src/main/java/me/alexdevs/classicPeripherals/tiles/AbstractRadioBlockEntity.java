package me.alexdevs.classicPeripherals.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.TowerNetwork;
import me.alexdevs.classicPeripherals.peripherals.RadioPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class AbstractRadioBlockEntity extends BlockEntity {
    public static final int MAX_HEIGHT = 24;
    public static final int MIN_HEIGHT = 2;
    public static final int SEGMENT_RANGE = 128;
    public static final double LOSS_FACTOR = 0.15;

    protected final Random random = new Random();

    protected int towerHeight = 1;
    protected boolean isValid = true;
    protected int channel = 0;
    protected BlockPos topPos;

    protected final RadioPeripheral peripheral = new RadioPeripheral(this);

    protected boolean initialized = false;

    public AbstractRadioBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        topPos = pos;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains("radio_channel")) {
            setChannel(nbt.getInt("radio_channel"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.putInt("radio_channel", getChannel());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        invalidate();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractRadioBlockEntity be) {
        if(!be.initialized) {
            be.initialized = true;
            be.validate();
        }
    }

    public boolean canBroadcast() {
        return isValid;
    }

    public void validate() {
        isValid = true;
        TowerNetwork.addTower(this);
    }

    public void invalidate() {
        isValid = false;
        TowerNetwork.removeTower(this);
    }

    public abstract void ping();

    public int getHeight() {
        return towerHeight;
    }

    public boolean isValid() {
        return isValid;
    }

    public BlockPos getTopPos() {
        return topPos;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public IPeripheral peripheral() {
        return peripheral;
    }

    public int getMaximumRange() {
        if (!isValid)
            return 0;

        return SEGMENT_RANGE * towerHeight;
    }

    public int getSafeRange() {
        var range = getMaximumRange();
        return range - (int) (range * LOSS_FACTOR);
    }

    public int getEffectiveMaxRange() {
        var y = this.getTopPos().getY();

        var range = getMaximumRange();

        if (y >= 96) {
            return range;
        }

        return Math.max(8, (int) (96 * (1 - Math.pow(Math.E, -0.05 * y)) / 100d * range));
    }

    public int getEffectiveSafeRange() {
        var effectiveRange = getEffectiveMaxRange();
        return effectiveRange - (int) (effectiveRange * LOSS_FACTOR);
    }

    public boolean inRange(AbstractRadioBlockEntity other) {
        var range = Math.max(this.getMaximumRange(), other.getMaximumRange());
        var distance = this.topPos.distSqr(other.topPos);
        return distance <= range * range;
    }

    public void receive(String message, double distance, AbstractRadioBlockEntity source) {
        if(!isValid()) {
            return;
        }

        ping();
        var safeRange = Math.max(this.getEffectiveSafeRange(), source.getEffectiveSafeRange());
        if (distance > safeRange) {
            var maxRange = Math.max(this.getEffectiveMaxRange(), source.getEffectiveMaxRange());
            var unsafeRange = maxRange - safeRange;
            var distanceInUnsafe = distance - safeRange;
            var corruption = distanceInUnsafe / unsafeRange;
            message = flipString(message, corruption);
        }

        final var data = message;
        peripheral.receive(data, distance);
    }

    protected String flipString(String data, double percentage) {
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
