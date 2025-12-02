package me.alexdevs.classicPeripherals.item;

import dan200.computercraft.api.ComputerCraftTags;
import me.alexdevs.classicPeripherals.utils.PocketUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NfcCardItem extends AbstractDataItem {
    public static final String INTERNAL_SIDE = "internal";

    public NfcCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player source, LivingEntity target, InteractionHand usedHand) {
        if (source.level().isClientSide()) {
            if (target instanceof Player) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        var data = AbstractDataItem.getData(stack);
        if (data.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!(target instanceof ServerPlayer player)) {
            return InteractionResult.PASS;
        }

        var inv = player.getInventory();
        if (inv.contains(ComputerCraftTags.Items.POCKET_COMPUTERS)) {
            var computers = PocketUtils.getAllPocketComputers(player);
            computers.forEach(computer -> computer.queueEvent("nfc_data", new Object[]{NfcCardItem.INTERNAL_SIDE, data.get()}));
        }

        return InteractionResult.CONSUME;
    }

}
