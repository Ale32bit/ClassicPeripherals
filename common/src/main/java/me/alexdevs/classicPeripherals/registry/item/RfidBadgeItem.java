package me.alexdevs.classicPeripherals.registry.item;

import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import me.alexdevs.classicPeripherals.platform.Services;
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

import java.util.List;
import java.util.Optional;

public class RfidBadgeItem extends Item implements IDataItem {
    public RfidBadgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (interactionTarget instanceof Player) {
            return InteractionResult.PASS;
        }

        var data = ItemDataHandler.getData(stack);
        if (data.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        var entity = (ILivingEntityMixin) interactionTarget;
        entity.setRfidData(data.get());

        if (!player.getAbilities().instabuild) {
            // stack.shrink wouldn't work :/
            player.getItemInHand(usedHand).shrink(1);
        }

        return InteractionResult.CONSUME;
    }

    public Optional<String> getEquippedData(ServerPlayer player) {
        if (!Services.EQUIPMENT.isLoaded()) {
            return Optional.empty();
        }
        return Services.EQUIPMENT.getEquippedStack(player, this)
                .flatMap(ItemDataHandler::getData);
    }

    @Override
    public boolean supportsPrivateKey() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
        if (tooltipFlag.isAdvanced()) {
            var id = ItemDataHandler.getId(stack);
            id.ifPresent(uuid -> components.add(Component.literal(uuid.toString()).withStyle(ChatFormatting.GRAY)));
        }
    }
}
