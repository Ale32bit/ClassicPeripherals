package me.alexdevs.classicPeripherals.peripherals.bluetooth;

import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.network.PacketNetwork;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.shared.peripheral.modem.ModemPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import me.alexdevs.classicPeripherals.mixinInterface.IEntityMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BluetoothApi extends ModemPeripheral implements ILuaAPI {
    private final IComputerSystem computer;
    private @Nullable Entity entity;

    public BluetoothApi(IComputerSystem computer) {
        super(new ModemState());

        this.computer = computer;
        entity = getEntity();
        attach(computer);
    }

    @Nullable
    public Entity getEntity() {
        if (isPocket()) {
            return getPocket().getEntity();
        }
        return null;
    }

    protected PacketNetwork createLocalNetwork() {
        var entity = (IEntityMixin) getEntity();
        if (entity != null) {
            return entity.classicPeripherals$getPacketNetwork();
        }

        return new LocalEntityNetwork();
    }

    // java moment
    @Override
    public String @NonNull [] getNames() {
        return new String[0];
    }

    @Override
    public String getModuleName() {
        return "os.bluetooth";
    }

    @Override
    public void update() {
        var currentEntity = getEntity();
        if (currentEntity != entity) {
            entity = currentEntity;
            attach(computer);
        }
    }

    @Override
    public void shutdown() {
        this.removed();
    }

    @Override
    protected @NonNull PacketNetwork getNetwork() {
        return createLocalNetwork();
    }

    @Override
    public double getRange() {
        return 1;
    }

    @Override
    public boolean isInterdimensional() {
        return false;
    }

    @Override
    public Level getLevel() {
        return computer.getLevel();
    }

    @Override
    public Vec3 getPosition() {
        if (isPocket()) {
            return getPocket().getPosition();
        }
        return computer.getPosition().getCenter();
    }

    @Override
    public boolean equals(@Nullable IPeripheral o) {
        return this.computer.equals(o);
    }

    private boolean isPocket() {
        return getPocket() != null;
    }

    private IPocketAccess getPocket() {
        return computer.getComponent(ComputerComponents.POCKET);
    }
}
