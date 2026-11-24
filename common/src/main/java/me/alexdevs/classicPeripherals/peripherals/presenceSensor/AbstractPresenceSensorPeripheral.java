package me.alexdevs.classicPeripherals.peripherals.presenceSensor;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ClassicPeripheralsConfig;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractPresenceSensorPeripheral implements IPeripheral {
    protected final AttachedComputerSet computers = new AttachedComputerSet();

    protected long scanIn = ClassicPeripherals.CONFIG.presenceSensorInterval;
    protected boolean manualScan = false;

    @Override
    public @NonNull String getType() {
        return "presence_sensor";
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

    protected List<PlayerData> scanPlayers() {
        var detectedPlayers = new ArrayList<PlayerData>();

        var level = getLevel();
        var origin = SableIntegration.getTranslatedPos(level, getPosition());

        if (level.isClientSide)
            return List.of();

        var range = ClassicPeripherals.CONFIG.presenceSensorRange;

        var nearbyPlayers = level.players().stream()
                .filter(player -> player.position().distanceToSqr(origin) <= range * range)
                .map(player -> (ServerPlayer) player)
                .toList();

        for (var player : nearbyPlayers) {
            if (canSee(player, origin, level)) {
                detectedPlayers.add(new PlayerData(player.getUUID(), player.getGameProfile().getName(), Math.sqrt(player.distanceToSqr(origin))));
            }
        }

        return detectedPlayers;
    }

    private boolean canSee(ServerPlayer player, Vec3 origin, BlockGetter level) {
        var castPoints = getPlayerCastPoints(player);

        for (var point : castPoints) {
            var ctx = new ClipContext(origin, point, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE,
                    player);

            var result = level.clip(ctx);
            if (result.getType() == BlockHitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    protected void updateState(boolean active) {

    }

    public void tick(Level level) {
        if (level.isClientSide) {
            return;
        }

        scanIn--;
        if (scanIn <= 0) {
            scanIn = ClassicPeripherals.CONFIG.presenceSensorInterval;
            if (!ClassicPeripherals.CONFIG.presenceSensorAutomaticScan && !manualScan) {
                return;
            }

            var players = scanPlayers();
            emitScanEvent(players, manualScan);
            manualScan = false;
        }
    }

    protected void emitScanEvent(List<PlayerData> players, boolean ignoreEmpty) {
        if (ignoreEmpty && players.size() == 0) {
            return;
        }

        var map = new HashMap<Integer, ObjectLuaTable>();
        for (var i = 1; i <= players.size(); i++) {
            var player = players.get(i - 1);
            map.put(i, new ObjectLuaTable(Map.of(
                    "uuid", player.uuid(),
                    "name", player.name(),
                    "distance", player.distance())));
        }

        var table = new ObjectLuaTable(map);

        computers.forEach(computer -> computer.queueEvent("presence_sense", computer.getAttachmentName(), table));
    }

    protected List<Vec3> getPlayerCastPoints(ServerPlayer player) {
        var aabb = player.getBoundingBox();

        return List.of(
                aabb.getBottomCenter().add(0, aabb.getYsize(), 0),
                player.getEyePosition(),
                aabb.getCenter(),
                aabb.getBottomCenter());
    }

    @LuaFunction
    public final MethodResult sense() {
        manualScan = true;
        scanIn = 4;
        return MethodResult.pullEvent("presence_sense", args -> {
            if (args.length != 3)
                return null;

            return MethodResult.of(args[2]);
        });
    }

    public record PlayerData(UUID uuid, String name, double distance) {
    }
}
