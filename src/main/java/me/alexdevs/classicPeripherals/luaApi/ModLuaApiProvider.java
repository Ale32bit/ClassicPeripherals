package me.alexdevs.classicPeripherals.luaApi;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponents;

public class ModLuaApiProvider {
    public static void initialize() {
        ComputerCraftAPI.registerAPIFactory(computer -> {
            var pocket = computer.getComponent(ComputerComponents.POCKET);
            return pocket != null ? new PocketNfcAPI(pocket) : null;
        });
    }
}
