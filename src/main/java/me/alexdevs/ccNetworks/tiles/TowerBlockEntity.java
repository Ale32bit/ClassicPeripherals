package me.alexdevs.ccNetworks.tiles;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.ccNetworks.block.ModBlocks;
import me.alexdevs.ccNetworks.core.TowerNetwork;
import me.alexdevs.ccNetworks.peripherals.RadioPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TowerBlockEntity extends BlockEntity {
    public static final int MAX_HEIGHT = 32;
    public static final int MIN_HEIGHT = 4;
    public static final int SEGMENT_RANGE = 128;
    public static final double LOSS_FACTOR = 0.25;

    private final Random random = new Random();

    private int towerHeight = 1;
    private boolean isValid = false;
    private int channel = 0;
    private BlockPos topPos;

    private RadioPeripheral peripheral = new RadioPeripheral(this);

    public TowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockTiles.TOWER_BASE, pos, state);
    }

    public void calculateTower() {
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

        isValid = true;

        this.topPos = pos.immutable();
        TowerNetwork.addTower(this);
    }

    public void invalidate() {
        TowerNetwork.removeTower(this);
    }

    public int getTowerHeight() {
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

    public boolean inRange(TowerBlockEntity other) {
        var range = Math.max(this.getMaximumRange(), other.getMaximumRange());
        var distance = this.topPos.distSqr(other.topPos);
        return distance <= range * range;
    }
    public void receive(String message, double distance, TowerBlockEntity source) {
        var safeRange = Math.max(this.getSafeRange(), source.getSafeRange());
        if (distance > safeRange) {
            var maxRange = Math.max(this.getMaximumRange(), source.getMaximumRange());
            var unsafeRange = maxRange - safeRange;
            var distanceInUnsafe = distance - safeRange;
            var corruption = distanceInUnsafe / unsafeRange;
            message = flipString(message, corruption);
        }

        final var data = message;
        peripheral.receive(data, distance);
    }

    private String flipString(String data, double percentage) {
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
