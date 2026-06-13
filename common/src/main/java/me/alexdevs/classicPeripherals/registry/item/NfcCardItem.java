package me.alexdevs.classicPeripherals.registry.item;

import dan200.computercraft.api.ComputerCraftTags;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.utils.PocketUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class NfcCardItem extends Item implements IDataItem {
    public static final String INTERNAL_SIDE = "internal";

    public NfcCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NonNull ItemStack stack, Player source, @NonNull LivingEntity target, @NonNull InteractionHand usedHand) {
        if (source.level().isClientSide()) {
            if (target instanceof Player) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        var data = ItemDataHandler.getData(stack);
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

    @Override
    public boolean supportsPrivateKey() {
        return true;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull List<Component> components, TooltipFlag tooltipFlag) {
        if (tooltipFlag.isAdvanced()) {
            var id = ItemDataHandler.getId(stack);
            id.ifPresent(uuid -> components.add(Component.literal(uuid.toString()).withStyle(ChatFormatting.GRAY)));
        }
    }
}