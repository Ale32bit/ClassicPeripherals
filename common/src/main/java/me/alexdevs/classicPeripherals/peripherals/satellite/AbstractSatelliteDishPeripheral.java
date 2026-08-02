package me.alexdevs.classicPeripherals.peripherals.satellite;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.core.dataHolder.DataHolderHandler;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteDevice;
import me.alexdevs.classicPeripherals.core.satellite.SatelliteNetwork;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

public abstract class AbstractSatelliteDishPeripheral extends SatelliteDevice implements IPeripheral {
    protected final AttachedComputerSet computers = new AttachedComputerSet();

    public AbstractSatelliteDishPeripheral() {
        super(ClassicPeripherals.getSatelliteNetwork(), SatelliteType.DISH);
    }

    @Override
    public @NonNull String getType() {
        return "satellite";
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
    public void tick(ServerLevel level) {

    }

    @LuaFunction
    public final void setChannel(int channel) {
        super.setChannel(channel);
    }

    @LuaFunction
    public final int getChannel() {
        return super.getChannel();
    }

    @LuaFunction
    public final void broadcast(String data) {
        super.broadcast(data);
    }
}
