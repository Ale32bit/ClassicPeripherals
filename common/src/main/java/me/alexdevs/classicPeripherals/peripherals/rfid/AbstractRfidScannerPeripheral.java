package me.alexdevs.classicPeripherals.peripherals.rfid;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRfidScannerPeripheral implements IPeripheral {
    public static final int SCAN_TIME = 4;

    protected final AttachedComputerSet computers = new AttachedComputerSet();

    protected long scanAt = 0;
    protected boolean scanScheduled = false;
    protected boolean scanStarted = false;

    @Override
    public @NonNull String getType() {
        return "rfid_scanner";
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);
    }

    public abstract Vec3 getPosition();

    public abstract Level getLevel();

    public boolean isScanning() {
        return scanScheduled;
    }

    protected List<ScannedRfidBadge> scanBadges() {
        var badges = new ArrayList<ScannedRfidBadge>();

        var level = getLevel();
        var origin = SableIntegration.getTranslatedPos(level, getPosition());

        if (level.isClientSide)
            return List.of();

        var range = ClassicPeripherals.CONFIG.rfidScanRange;

        // Scan player inventories
        var nearbyPlayers = level.players().stream()
                .filter(player -> player.position().distanceToSqr(origin) <= range * range)
                .map(player -> (ServerPlayer) player)
                .toList();

        for (var player : nearbyPlayers) {
            var inventory = player.getInventory();
            var distance = player.position().distanceTo(origin);
            for (var stack : inventory.items) {
                if (!stack.is(ModRegistry.Items.RFID_BADGE.get())) {
                    continue;
                }

                var data = DataHolderHandler.getData(stack);
                if (data.isEmpty()) {
                    continue;
                }

                data.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
            }

            var equippedData = ModRegistry.Items.RFID_BADGE.get().getEquippedData(player);
            equippedData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
        }

        var aabb = AABB.ofSize(origin, range, range, range);
        var nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);

        for (var livingEntity : nearbyEntities) {
            // Skip players because already fully scanned
            if (livingEntity instanceof Player) {
                continue;
            }

            var distance = livingEntity.position().distanceTo(origin);

            // Scan living entities with RFID injected
            var entity = (ILivingEntityMixin) livingEntity;
            var injectedData = entity.getRfidData();
            injectedData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));

            // Scan living entities holding RFID badge
            if (livingEntity.isHolding(ModRegistry.Items.RFID_BADGE.get())) {
                var handSlots = livingEntity.getHandSlots();
                for (var handStack : handSlots) {
                    if (handStack.is(ModRegistry.Items.RFID_BADGE.get())) {
                        var data = DataHolderHandler.getData(handStack);
                        data.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
                    }
                }
            }
        }

        // Dropped RFID badges
        var nearbyDroppedItems = level.getEntitiesOfClass(ItemEntity.class, aabb);
        for (var droppedItem : nearbyDroppedItems) {
            var stack = droppedItem.getItem();
            if (!stack.is(ModRegistry.Items.RFID_BADGE.get())) {
                continue;
            }

            var distance = droppedItem.position().distanceTo(origin);
            var badgeData = DataHolderHandler.getData(stack);
            badgeData.ifPresent(s -> badges.add(new ScannedRfidBadge(s, distance)));
        }

        return badges;
    }

    protected void updateState(boolean active) {

    }

    public void tick(Level level) {
        if (level.isClientSide) {
            return;
        }

        if (!scanScheduled) {
            return;
        }

        if (scanStarted) {
            scanStarted = false;
            updateState(true);
        }

        if (level.getGameTime() >= scanAt) {
            scanScheduled = false;
            var badges = scanBadges();
            emitScanEvent(badges);
            updateState(false);
        }
    }

    public void scheduleScan() {
        scanAt = getLevel().getGameTime() + SCAN_TIME;
        scanScheduled = true;
        scanStarted = true;
    }

    protected void emitScanEvent(List<ScannedRfidBadge> badges) {
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
        scheduleScan();
        return MethodResult.pullEvent("rfid_scan", args -> {
            if (args.length != 3)
                return null;

            return MethodResult.of(args[2]);
        });
    }

    public record ScannedRfidBadge(String data, double distance) {
    }
}
