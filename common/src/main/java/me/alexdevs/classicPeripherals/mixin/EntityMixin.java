package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.api.network.PacketNetwork;
import me.alexdevs.classicPeripherals.mixinInterface.IEntityMixin;
import me.alexdevs.classicPeripherals.peripherals.ban.LocalEntityNetwork;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin {
    @Unique
    private PacketNetwork classicPeripherals$packetNetwork = null;

    @Override
    public @NonNull PacketNetwork classicPeripherals$getPacketNetwork() {
        if (classicPeripherals$packetNetwork == null) {
            classicPeripherals$packetNetwork = new LocalEntityNetwork();
        }
        
        return classicPeripherals$packetNetwork;
    }
}
