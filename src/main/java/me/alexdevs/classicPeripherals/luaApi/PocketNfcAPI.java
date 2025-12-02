package me.alexdevs.classicPeripherals.luaApi;

import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.shared.pocket.core.PocketBrain;
import org.jetbrains.annotations.Nullable;

public class PocketNfcAPI implements ILuaAPI {
    private final IPocketAccess pocket;

    public PocketNfcAPI(IPocketAccess pocket) {
        this.pocket = pocket;
    }

    private PocketBrain getBrain() {
        return (PocketBrain) pocket;
    }

    @Override
    public String[] getNames() {
        return new String[0];
    }

    @Override
    public String getModuleName() {
        return "os.nfc";
    }

    @Override
    public void shutdown() {
        var id = getBrain().computer().getID();
        PocketNfcAccess.set(id, null);
    }

    @LuaFunction
    public final void write(@Nullable String data) {
        var id = getBrain().computer().getID();
        PocketNfcAccess.set(id, data);
    }

    @LuaFunction
    public final @Nullable String pop() {
        var id = getBrain().computer().getID();
        var poppedData = PocketNfcAccess.get(id);
        PocketNfcAccess.set(id, null);
        return poppedData.orElse(null);
    }
}
