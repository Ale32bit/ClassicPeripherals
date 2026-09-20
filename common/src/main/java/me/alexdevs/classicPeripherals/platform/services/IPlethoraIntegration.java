package me.alexdevs.classicPeripherals.platform.services;

import dan200.computercraft.api.lua.IComputerSystem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface IPlethoraIntegration {
    boolean isNeural(IComputerSystem system);

    Vec3 getPosition(IComputerSystem system);

    @Nullable
    Entity getEntity(IComputerSystem system);
}
