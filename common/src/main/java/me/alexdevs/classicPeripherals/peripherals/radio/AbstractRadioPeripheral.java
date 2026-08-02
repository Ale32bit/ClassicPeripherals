package me.alexdevs.classicPeripherals.peripherals.radio;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.RadioNetwork;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import oshi.annotation.concurrent.GuardedBy;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.SplittableRandom;

public abstract class AbstractRadioPeripheral implements IPeripheral {
    private final @GuardedBy("computers") AttachedComputerSet computers = new AttachedComputerSet();
    private int channel = 0;
    private final SplittableRandom rng = new SplittableRandom();

    @Override
    public @NonNull String getType() {
        return "radio_tower";
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);

        var radioNetwork = ClassicPeripherals.getRadioNetwork();
        Objects.requireNonNull(radioNetwork);
        radioNetwork.addReceiver(this);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);

        if (!computers.hasComputers()) {
            var radioNetwork = ClassicPeripherals.getRadioNetwork();
            if (radioNetwork != null) {
                radioNetwork.removeReceiver(this);
            }
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
        byte[] bytes = data.getBytes(StandardCharsets.ISO_8859_1);
        long total = (long) bytes.length << 3;
        long toFlip = (long) Math.ceil(total * percentage);
        if (toFlip <= 0) return data;

        long i = 0;
        for (; i + 1 < toFlip; i += 2) {
            long r = rng.nextLong();
            long a = ((r >>> 32) * total) >>> 32;
            long b = ((r & 0xFFFFFFFFL) * total) >>> 32;
            bytes[(int) (a >>> 3)] ^= (byte) (1 << (a & 7));
            bytes[(int) (b >>> 3)] ^= (byte) (1 << (b & 7));
        }

        if (i < toFlip) {
            long a = (((rng.nextLong() >>> 32)) * total) >>> 32;
            bytes[(int) (a >>> 3)] ^= (byte) (1 << (a & 7));
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

        var radioNetwork = ClassicPeripherals.getRadioNetwork();
        Objects.requireNonNull(radioNetwork);
        radioNetwork.broadcast(this, data, getRange());
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
