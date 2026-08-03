package me.alexdevs.classicPeripherals.peripherals.satellite.launcher;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.core.satellite.Satellite;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteNetwork;
import me.alexdevs.classicPeripherals.utils.LuaUtils;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SatelliteLauncherPeripheral implements IPeripheral {
    private final SatelliteLauncherBlockEntity launcher;

    public SatelliteLauncherPeripheral(SatelliteLauncherBlockEntity launcher) {
        this.launcher = launcher;
    }

    @Override
    public @NonNull String getType() {
        return "satellite_launcher";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof SatelliteLauncherPeripheral o && launcher == o.launcher;
    }

    @LuaFunction(mainThread = true)
    public final String launch() throws LuaException {
        if (!launcher.isReady()) {
            throw new LuaException("Satellite launcher is not ready.");
        }

        var uuid = launcher.launch();

        return uuid
                .orElseThrow(() -> new LuaException("Failed to launch satellite."))
                .toString();
    }

    @LuaFunction
    public final boolean isReady() {
        return launcher.isReady();
    }

    @LuaFunction
    public final void setMode(Satellite.RuntimeType type) {
        launcher.setMode(type);
    }

    @LuaFunction
    public final String getMode() {
        return launcher.getMode().name();
    }

    @LuaFunction
    public final void setChannel(int channel) throws LuaException {
        LuaUtils.assertRange(0, channel, SatelliteNetwork.MIN_CHANNEL, SatelliteNetwork.MAX_CHANNEL);
        launcher.setChannel(channel);
    }

    @LuaFunction
    public final int getChannel() {
        return launcher.getChannel();
    }

    @LuaFunction
    public final void setFirmware(String firmware) {
        // todo: implement runtime
    }
}
