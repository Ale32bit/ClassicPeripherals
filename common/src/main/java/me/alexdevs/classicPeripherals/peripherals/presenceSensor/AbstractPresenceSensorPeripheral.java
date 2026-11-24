package me.alexdevs.classicPeripherals.peripherals.presenceSensor;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.*;

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

    protected List<EntityPresenceData> scanEntities() {
        var detectedEntities = new ArrayList<EntityPresenceData>();

        var level = getLevel();
        var origin = SableIntegration.getTranslatedPos(level, getPosition());

        if (level.isClientSide)
            return List.of();

        var range = ClassicPeripherals.CONFIG.presenceSensorRange;

        var aabb = AABB.ofSize(origin, range, range, range);
        var nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);

        for (var entity : nearbyEntities) {
            if (canSee(entity, origin, level)) {
                detectedEntities.add(new EntityPresenceData(EntityType.getKey(entity.getType()).toString(), Math.sqrt(entity.distanceToSqr(origin))));
            }
        }

        return detectedEntities;
    }

    private boolean canSee(LivingEntity entity, Vec3 origin, BlockGetter level) {
        var castPoints = getPlayerCastPoints(entity);

        for (var point : castPoints) {
            var ctx = new ClipContext(origin, point, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity);

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

            var entities = scanEntities();
            emitScanEvent(entities, !manualScan);
            manualScan = false;
        }
    }

    protected void emitScanEvent(List<EntityPresenceData> entities, boolean ignoreEmpty) {
        if (ignoreEmpty && entities.isEmpty()) {
            return;
        }

        var map = new HashMap<Integer, ObjectLuaTable>();
        for (var i = 1; i <= entities.size(); i++) {
            var entity = entities.get(i - 1);
            map.put(i, new ObjectLuaTable(Map.of(
                    "type", entity.type(),
                    "distance", entity.distance())));
        }

        var table = new ObjectLuaTable(map);

        computers.forEach(computer -> computer.queueEvent("presence_sense", computer.getAttachmentName(), table));
    }

    protected List<Vec3> getPlayerCastPoints(LivingEntity entity) {
        var aabb = entity.getBoundingBox();

        return List.of(
                aabb.getBottomCenter().add(0, aabb.getYsize(), 0),
                entity.getEyePosition(),
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

    public record EntityPresenceData(String type, double distance) {
    }
}
