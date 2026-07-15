package me.alexdevs.classicPeripherals.peripherals.hologram;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.utils.VolumetricArray;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import oshi.annotation.concurrent.GuardedBy;

public class HologramProjectorPeripheral implements IPeripheral {
    public static final int RESOLUTION = 48;

    private final HologramProjectorBlockEntity hologramProjector;
    private final @GuardedBy("computers") AttachedComputerSet computers = new AttachedComputerSet();

    private final VolumetricArray hologram = new VolumetricArray(RESOLUTION);

    public HologramProjectorPeripheral(HologramProjectorBlockEntity hologramProjector) {
        this.hologramProjector = hologramProjector;
    }

    @Override
    public @NonNull String getType() {
        return "hologram";
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof HologramProjectorPeripheral o && this.hologramProjector == o.hologramProjector;
    }

    @LuaFunction
    public final void clear() {
        hologram.clear();
    }

    @LuaFunction
    public final int get(int x, int y, int z) {
        return hologram.get(x, y, z);
    }

    @LuaFunction
    public final void set(int x, int y, int z, int value) {
        hologram.set(x, y, z, value);
    }
}
