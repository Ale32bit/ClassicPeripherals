package me.alexdevs.classicPeripherals.mixinInterface;

import dan200.computercraft.api.network.PacketNetwork;
import me.alexdevs.classicPeripherals.peripherals.ban.LocalEntityNetwork;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface IEntityMixin {
    @NonNull
    LocalEntityNetwork classicPeripherals$getPacketNetwork();
}
