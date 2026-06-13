package me.alexdevs.classicPeripherals.mixin;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import me.alexdevs.classicPeripherals.peripherals.nfc.item.NfcCardItem;
import me.alexdevs.classicPeripherals.peripherals.nfc.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.utils.PocketUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class)
public abstract class ItemMixin {
    // there has to be a better way...
    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    private void classicperipherals$pocketInteractPlayer(ItemStack stack, Player source, LivingEntity target, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        var self = (Item) (Object) this;
        if (!(self instanceof PocketComputerItem)) {
            return;
        }

        if(!source.isCrouching()) {
            return;
        }

        if (source.level().isClientSide()) {
            if (target instanceof Player) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            return;
        }

        var id = PocketUtils.getServerComputer(source.getServer(), stack).getID();
        var data = PocketNfcAccess.pop(id);
        if (data.isEmpty()) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        if (!(target instanceof ServerPlayer player)) {
            cir.setReturnValue(InteractionResult.PASS);
            return;
        }

        var inv = player.getInventory();
        if (inv.contains(ComputerCraftTags.Items.POCKET_COMPUTERS)) {
            var computers = PocketUtils.getAllPocketComputers(player);
            computers.forEach(computer -> computer.queueEvent("nfc_data", new Object[]{NfcCardItem.INTERNAL_SIDE, data.get()}));
        }

        cir.setReturnValue(InteractionResult.CONSUME);
    }
}
