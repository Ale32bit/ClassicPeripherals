package me.alexdevs.classicPeripherals.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.TowerNetwork;
import me.alexdevs.classicPeripherals.tiles.AbstractRadioBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import oshi.annotation.concurrent.GuardedBy;

public class RadioPeripheral implements IPeripheral {
    private final AbstractRadioBlockEntity tile;
    private final @GuardedBy("computers") AttachedComputerSet computers = new AttachedComputerSet();
    private int channel = 0;

    public RadioPeripheral(AbstractRadioBlockEntity tile) {
        this.tile = tile;
    }

    @Override
    public String getType() {
        return "radio_tower";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || (other instanceof RadioPeripheral o && tile == o.tile);
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);

        TowerNetwork.addReceiver(this);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);

        if (!computers.hasComputers()) {
            TowerNetwork.removeReceiver(this);
        }
    }

    public Level getLevel() {
        return tile.getLevel();
    }

    public Vec3 getPosition() {
        return Vec3.atLowerCornerOf(tile.getBlockPos());
    }

    public double getRange() {
        return tile.getEffectiveMaxRange();
    }

    public AbstractRadioBlockEntity getTile() {
        return tile;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void receive(String data, double distance, double range) {
        if (!isValid()) {
            return;
        }

        var safeRange = AbstractRadioBlockEntity.getSafeRange(range);
        if (distance > safeRange) {
            var unsafeRange = range - safeRange;
            var distanceInUnsafe = distance - safeRange;
            var corruption = distanceInUnsafe / unsafeRange;
            data = tile.flipString(data, corruption);
        }

        final var mutatedData = data;

        synchronized (computers) {
            computers.forEach(computer ->
                    computer.queueEvent("radio_message", computer.getAttachmentName(), mutatedData, distance));
        }

        tile.ping();
    }

    @LuaFunction
    public final boolean isValid() {
        return tile.isValid();
    }

    @LuaFunction
    public final void broadcast(String data) throws LuaException {
        if (!tile.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        if (!tile.canBroadcast()) {
            throw new LuaException("This antenna is not capable of broadcasting.");
        }

        TowerNetwork.broadcast(this, data, tile.getEffectiveMaxRange());
        tile.ping();
    }

    @LuaFunction
    public final boolean canBroadcast() {
        return tile.canBroadcast();
    }

    @LuaFunction(mainThread = true)
    public final void setFrequency(ILuaContext context, int frequency) throws LuaException {
        if (frequency < TowerNetwork.MIN_FREQUENCY || frequency > TowerNetwork.MAX_FREQUENCY) {
            throw new LuaException("Frequency out of range. Must be between " + TowerNetwork.MIN_FREQUENCY + " and " + TowerNetwork.MAX_FREQUENCY + ".");
        }

        if (!tile.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = TowerNetwork.getChannel(frequency);
        this.setChannel(channel);
    }

    @LuaFunction
    public final int getFrequency() throws LuaException {
        if (!tile.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = this.getChannel();
        return TowerNetwork.getFrequency(channel);
    }

    @LuaFunction
    public final int getHeight() throws LuaException {
        if (!tile.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        return tile.getHeight();
    }
}
