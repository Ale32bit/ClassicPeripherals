package me.alexdevs.classicPeripherals.registry.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.RadioNetwork;
import me.alexdevs.classicPeripherals.registry.tiles.AbstractRadioBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import oshi.annotation.concurrent.GuardedBy;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class AbstractRadioPeripheral implements IPeripheral {
    private final @GuardedBy("computers") AttachedComputerSet computers = new AttachedComputerSet();
    private int channel = 0;
    protected final Random random = new Random();

    @Override
    public String getType() {
        return "radio_tower";
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);

        RadioNetwork.addReceiver(this);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);

        if (!computers.hasComputers()) {
            RadioNetwork.removeReceiver(this);
        }
    }

    public abstract boolean isValid();

    public abstract Level getLevel();

    public abstract Vec3 getPosition();

    public abstract double getRange();

    public abstract void ping();

    public abstract boolean canBroadcast();

    public int getHeight() {
        return 1;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String flipString(String data, double percentage) {
        var bytes = data.getBytes(StandardCharsets.ISO_8859_1);
        var total = bytes.length * 8;
        var toFlip = (int) Math.ceil(total * percentage);

        for (int i = 0; i < toFlip; i++) {
            var bit = random.nextInt(total);
            var byteIndex = bit / 8;
            var bitIndex = bit % 8;
            bytes[byteIndex] ^= (byte) (1 << bitIndex);
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
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
            data = flipString(data, corruption);
        }

        final var mutatedData = data;

        synchronized (computers) {
            computers.forEach(computer ->
                    computer.queueEvent("radio_message", computer.getAttachmentName(), mutatedData, distance));
        }

        ping();
    }

    @LuaFunction("isValid")
    public final boolean LuaIsValid() {
        return isValid();
    }

    @LuaFunction("broadcast")
    public final void LuaBroadcast(String data) throws LuaException {
        if (!isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        if (!canBroadcast()) {
            throw new LuaException("This antenna is not capable of broadcasting.");
        }

        RadioNetwork.broadcast(this, data, getRange());
        ping();
    }

    @LuaFunction("canBroadcast")
    public final boolean LuaCanBroadcast() {
        return canBroadcast();
    }

    @LuaFunction(mainThread = true, value = "setFrequency")
    public final void LuaSetFrequency(ILuaContext context, int frequency) throws LuaException {
        if (frequency < RadioNetwork.MIN_FREQUENCY || frequency > RadioNetwork.MAX_FREQUENCY) {
            throw new LuaException("Frequency out of range. Must be between " + RadioNetwork.MIN_FREQUENCY + " and " + RadioNetwork.MAX_FREQUENCY + ".");
        }

        if (!isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = RadioNetwork.getChannel(frequency);
        this.setChannel(channel);
    }

    @LuaFunction("getFrequency")
    public final int LuaGetFrequency() throws LuaException {
        if (!isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = this.getChannel();
        return RadioNetwork.getFrequency(channel);
    }

    @LuaFunction("getHeight")
    public final int LuaGetHeight() throws LuaException {
        if (!isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        return getHeight();
    }
}
