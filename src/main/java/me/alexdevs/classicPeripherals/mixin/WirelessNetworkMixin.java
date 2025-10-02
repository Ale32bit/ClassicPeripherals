package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.api.network.Packet;
import dan200.computercraft.api.network.PacketReceiver;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessNetwork;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.awt.*;

@Mixin(WirelessNetwork.class)
public abstract class WirelessNetworkMixin {
    /**
     * @author Alessandro "AlexDevs" Proto
     * @reason Nerfing the ender modem.
     */
    @Overwrite(remap = false)
    private static void tryTransmit(PacketReceiver receiver, Packet packet, double range, boolean interdimensional) {
        var sender = packet.sender();
        if (receiver.getLevel() == sender.getLevel()) {
            var receiveRange = Math.max(range, receiver.getRange()); // Ensure range is symmetrical
            var distanceSq = receiver.getPosition().distanceToSqr(sender.getPosition());
            if (distanceSq <= receiveRange * receiveRange) {
                receiver.receiveSameDimension(packet, Math.sqrt(distanceSq));
            }
        } else {
            // Only ender wireless modems are capable of sending to other dimensions, but only if the block position is within 8 blocks.
            if (interdimensional) {
                var originLevel = packet.sender().getLevel();
                var destinationLevel = receiver.getLevel();

                // Apply dimensional coordinate scaling. (i.e., overworld / nether = 1 / 8)
                var scale = DimensionType.getTeleportationScale(originLevel.dimensionType(), destinationLevel.dimensionType());
                var receiverPos = receiver.getPosition().multiply(scale, 1d, scale);
                var distanceSq = receiverPos.distanceToSqr(sender.getPosition());

                if(Math.sqrt(distanceSq) <= 8d) {
                    receiver.receiveDifferentDimension(packet);
                }
            }
        }
    }
}
