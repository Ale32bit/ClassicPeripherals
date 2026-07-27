package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.api.network.Packet;
import dan200.computercraft.api.network.PacketReceiver;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessNetwork;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.integrations.SableIntegration;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(WirelessNetwork.class)
public abstract class WirelessNetworkMixin {
    @Inject(method = "tryTransmit", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void tryTransmit(PacketReceiver receiver, Packet packet, double range, boolean interdimensional, CallbackInfo ci) {
        if (!ClassicPeripherals.CONFIG.enderModemNerf) {
            return;
        }

        ci.cancel();

        var sender = packet.sender();
        if (receiver.getLevel() == sender.getLevel()) {
            var receiveRange = Math.max(range, receiver.getRange()); // Ensure range is symmetrical
            var distanceSq = SableIntegration.getDistanceSquared(receiver.getLevel(), receiver.getPosition(), sender.getPosition());
            if (distanceSq <= receiveRange * receiveRange) {
                receiver.receiveSameDimension(packet, Math.sqrt(distanceSq));
            }
        } else {
            // Only ender wireless modems are capable of sending to other dimensions, but only if the block position is within 8 blocks.
            if (interdimensional) {
                var originLevel = packet.sender().getLevel();
                var destinationLevel = receiver.getLevel();

                // Apply dimensional coordinate scaling. (i.e., overworld / nether = 8 / 1)
                var scale = DimensionType.getTeleportationScale(originLevel.dimensionType(), destinationLevel.dimensionType());
                var scaledSenderPos = sender.getPosition().multiply(scale, 1d, scale);
                var distanceSq = SableIntegration.getDistanceSquared(receiver.getLevel(), scaledSenderPos, receiver.getPosition());

                if (Math.sqrt(distanceSq) <= ClassicPeripherals.CONFIG.enderModemCrossDimensionalRange) {
                    receiver.receiveDifferentDimension(packet);
                }
            }
        }
    }
}
