package me.alexdevs.classicPeripherals.mixinInterface;

import dan200.computercraft.api.network.PacketNetwork;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface IEntityMixin {
    @NonNull
    PacketNetwork classicPeripherals$getPacketNetwork();
}
