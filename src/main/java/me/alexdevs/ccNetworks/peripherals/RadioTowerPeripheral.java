package me.alexdevs.ccNetworks.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.ccNetworks.core.TowerNetwork;
import me.alexdevs.ccNetworks.tiles.TowerBaseBlockEntity;
import org.jspecify.annotations.Nullable;

public class RadioTowerPeripheral implements IPeripheral {
    private final TowerBaseBlockEntity radioTower;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public RadioTowerPeripheral(TowerBaseBlockEntity radioTower) {
        this.radioTower = radioTower;
    }

    private void update() {
        var tower = this.radioTower.getTower();

        if(tower != null) {
            tower.setPeripheral(this);
        }
    }

    @Override
    public String getType() {
        return "radio_tower";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof RadioTowerPeripheral o && radioTower == o.radioTower;
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

    @LuaFunction
    public final boolean isValid() {
        update();
        return radioTower.isValid();
    }

    @LuaFunction
    public final void broadcast(String data) {
        update();
        TowerNetwork.broadcast(radioTower.getTower(), data);
    }

    public void receive(String data, double distance) {
        update();
        computers.forEach(x -> x.queueEvent("radio_message", data, distance));
    }
}
