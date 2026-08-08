package me.alexdevs.classicPeripherals.peripherals.satellite.dish;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteDevice;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteNetwork;
import me.alexdevs.classicPeripherals.utils.LuaUtils;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.NonNull;

import java.util.ArrayDeque;
import java.util.Queue;

public abstract class AbstractSatelliteDishPeripheral extends SatelliteDevice implements IPeripheral {
    protected final AttachedComputerSet computers = new AttachedComputerSet();

    public AbstractSatelliteDishPeripheral() {
        super(ClassicPeripherals.getSatelliteNetwork(), SatelliteType.DISH);
    }

    @Override
    public @NonNull String getType() {
        return "satellite";
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);

        ClassicPeripherals.getSatelliteNetwork().addSatellite(this);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);

        if (!computers.hasComputers()) {
            ClassicPeripherals.getSatelliteNetwork().removeSatellite(this);
        }
    }

    @Override
    public void onDataReceived(String data, SatelliteDevice source) {
        if (canTransreceive()) {
            var distance = source.getPosition().distanceTo(getPosition());
            computers.forEach(computer -> computer.queueEvent("satellite_message", computer.getAttachmentName(), data, distance));
        }
    }

    @Override
    public void broadcast(String data) {
        if (canTransreceive()) {
            super.broadcast(data);
        }

    }

    public abstract boolean canTransreceive();

    @LuaFunction(value = "setChannel", mainThread = true)
    public final void luaSetChannel(int channel) throws LuaException {
        LuaUtils.assertRange(0, channel, SatelliteNetwork.MIN_CHANNEL, SatelliteNetwork.MAX_CHANNEL);

        setChannel(channel);
    }

    @LuaFunction("getChannel")
    public final int luaGetChannel() {
        return getChannel();
    }

    @LuaFunction("broadcast")
    public final void luaBroadcast(String data) {
        broadcast(data);
    }
}
