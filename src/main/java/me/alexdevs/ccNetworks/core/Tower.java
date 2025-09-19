package me.alexdevs.ccNetworks.core;

import me.alexdevs.ccNetworks.peripherals.RadioTowerPeripheral;
import me.alexdevs.ccNetworks.tiles.TowerBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Tower {
    private int channel = 0;
    private int tickCooldown;
    private final int height;
    private final BlockPos basePos;
    private final BlockPos topPos;
    private final Level level;
    private final TowerBaseBlockEntity be;
    private RadioTowerPeripheral peripheral;

    public Tower(TowerBaseBlockEntity be) {
        this.height = be.getTowerHeight();
        this.basePos = be.getBlockPos();
        this.level = be.getLevel();

        this.topPos = basePos.above(height - 1);

        this.be = be;
    }

    public TowerBaseBlockEntity getBlockEntity() {
        return be;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getHeight() {
        return height;
    }

    public BlockPos getBasePos() {
        return basePos;
    }

    public BlockPos getTopPos() {
        return topPos;
    }

    public Level getLevel() {
        return level;
    }

    public int getRange() {
        return be.getRange();
    }

    public boolean inRange(Tower other) {
        var range = Math.max(this.getRange(), other.getRange());
        var distance = this.topPos.distSqr(other.topPos);
        return distance <= range * range;
    }

    public void setPeripheral(RadioTowerPeripheral peripheral) {
        this.peripheral = peripheral;
    }

    public RadioTowerPeripheral getPeripheral() {
        return peripheral;
    }

    public void receive(String message, double distance) {
        peripheral.receive(message, distance);
    }
}
