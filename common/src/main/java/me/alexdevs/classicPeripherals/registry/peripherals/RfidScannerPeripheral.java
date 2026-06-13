package me.alexdevs.classicPeripherals.registry.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.registry.tiles.RfidScannerBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RfidScannerPeripheral implements IPeripheral {
    private final RfidScannerBlockEntity rfidScanner;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public RfidScannerPeripheral(RfidScannerBlockEntity rfidScanner) {
        this.rfidScanner = rfidScanner;
    }

    @Override
    public String getType() {
        return "rfid_scanner";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof RfidScannerPeripheral o && rfidScanner == o.rfidScanner;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    public Vec3 getPosition() {
        return Vec3.atLowerCornerOf(rfidScanner.getBlockPos().relative(rfidScanner.getDirection()));
    }

    public void emitScanEvent(List<RfidScannerBlockEntity.ScannedRfidBadge> badges) {
        var map = new HashMap<Integer, ObjectLuaTable>();
        for (var i = 1; i <= badges.size(); i++) {
            var badge = badges.get(i - 1);
            map.put(i, new ObjectLuaTable(Map.of(
                    "data", badge.data(),
                    "distance", badge.distance()
            )));
        }

        var table = new ObjectLuaTable(map);

        computers.forEach(computer -> computer.queueEvent("rfid_scan", computer.getAttachmentName(), table));
    }

    @LuaFunction
    public final MethodResult scan() {
        rfidScanner.scheduleScan();
        return MethodResult.pullEvent("rfid_scan", args -> {
            if (args.length != 3)
                return null;

            return MethodResult.of(args[2]);
        });
    }
}
