package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.shared.peripheral.redstone.RedstoneRelayPeripheral;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RedstoneRelayPeripheral.class, remap = false)
public abstract class RedstoneRelayPeripheralMixin {
    @Shadow
    @Final
    private AttachedComputerSet computers;

    @Inject(method = "queueRedstoneEvent", at = @At("HEAD"), cancellable = true)
    private void classicperipherals$addNetworkNameEvent(CallbackInfo ci) {
        computers.forEach(computer -> computer.queueEvent("redstone", computer.getAttachmentName()));
        ci.cancel();
    }
}
