package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.shared.config.Config;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WirelessModemPeripheral.class, remap = false)
public abstract class WirelessModemPeripheralMixin {
    @Final
    @Shadow
    private boolean advanced;

    @Redirect(
            method = "getRange",
            at = @At(
                    value = "FIELD",
                    target = "Ldan200/computercraft/shared/peripheral/modem/wireless/WirelessModemPeripheral;advanced:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean classicperipherals$overrideGetRangeAdvanced(WirelessModemPeripheral instance) {
        return false;
    }

    @Inject(method = "getRange", at = @At("RETURN"), cancellable = true)
    private void classicperipherals$getRangeValue(CallbackInfoReturnable<Double> cir) {
        if (!advanced)
            return;

        var self = (WirelessModemPeripheral)(Object)this;
        var world = self.getLevel();
        if (world != null) {
            var position = self.getPosition();
            double minRange = Config.modemRange;
            double maxRange = Config.modemHighAltitudeRange;
            double range;
            if (position.y > 96.0 && maxRange > minRange) {
                range = minRange + (position.y - 96.0) * ((maxRange - minRange) / ((world.getMaxBuildHeight() - 1) - 96.0));
            } else {
                range = minRange;
            }
            cir.setReturnValue(range * 2d);
            return;
        }
        cir.setReturnValue(0.0);
    }
}