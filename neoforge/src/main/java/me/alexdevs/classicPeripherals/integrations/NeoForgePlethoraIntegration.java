package me.alexdevs.classicPeripherals.integrations;

import dan200.computercraft.api.lua.IComputerSystem;
import me.alexdevs.classicPeripherals.platform.services.IPlethoraIntegration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class NeoForgePlethoraIntegration implements IPlethoraIntegration {
    @Override
    public boolean isNeural(IComputerSystem system) {
        return false;
    }

    @Override
    public Vec3 getPosition(IComputerSystem system) {
        return system.getPosition().getCenter();
    }

    @Override
    public @Nullable Entity getEntity(IComputerSystem system) {
        return null;
    }
}
