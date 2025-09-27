package me.alexdevs.ccNetworks.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.ccNetworks.core.TowerNetwork;
import me.alexdevs.ccNetworks.tiles.TowerBlockEntity;
import org.jspecify.annotations.Nullable;

public class RadioPeripheral implements IPeripheral {
    private final TowerBlockEntity radioTower;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public RadioPeripheral(TowerBlockEntity radioTower) {
        this.radioTower = radioTower;
    }

    private void update() {
        this.radioTower.calculateTower();
    }

    @Override
    public String getType() {
        return "radio_tower";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof RadioPeripheral o && radioTower == o.radioTower;
    }

    @Override
    public void attach(IComputerAccess computer) {
        update();
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        update();
        computers.remove(computer);
    }

    public void receive(String data, double distance) {
        update();
        computers.queueEvent("radio_message", data, distance);
    }

    @LuaFunction
    public final boolean isValid() {
        update();
        return radioTower.isValid();
    }

    @LuaFunction()
    public final void broadcast(String data) throws LuaException {
        update();

        if (!radioTower.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        radioTower.ping();
        TowerNetwork.broadcast(radioTower, data);
    }

    @LuaFunction(mainThread = true)
    public final void setFrequency(ILuaContext context, int frequency) throws LuaException {
        if (frequency < TowerNetwork.MIN_FREQUENCY || frequency > TowerNetwork.MAX_FREQUENCY) {
            throw new LuaException("Frequency out of range. Must be between " + TowerNetwork.MIN_FREQUENCY + " and " + TowerNetwork.MAX_FREQUENCY + ".");
        }

        if (!radioTower.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = TowerNetwork.getChannel(frequency);
        radioTower.setChannel(channel);
    }

    @LuaFunction
    public final int getFrequency() throws LuaException {
        if (!radioTower.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        var channel = radioTower.getChannel();
        return TowerNetwork.getFrequency(channel);
    }

    @LuaFunction
    public final Object[] getRange() throws LuaException {
        if (!radioTower.isValid()) {
            throw new LuaException("The radio tower is not built correctly.");
        }

        return new Object[]{
                radioTower.getMaximumRange(),
                radioTower.getEffectiveMaxRange(),
        };
    }
}
