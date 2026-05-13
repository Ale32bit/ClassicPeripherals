package me.alexdevs.classicPeripherals.item;

import dev.emi.trinkets.api.TrinketsApi;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import me.alexdevs.classicPeripherals.integrations.TrinketsIntegration;
import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

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
        if (!TrinketsIntegration.isLoaded()) {
            return Optional.empty();
        }

        var trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isEmpty()) {
            return Optional.empty();
        }

        if (!trinkets.get().isEquipped(this)) {
            return Optional.empty();
        }

        var equipped = trinkets.get().getEquipped(this);
        var badge = equipped.stream().findFirst();
        if (badge.isEmpty()) {
            return Optional.empty();
        }

        var stack = badge.get().getB();
        return ItemDataHandler.getData(stack);
    }

    @Override
    public boolean supportsPrivateKey() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (tooltipFlag.isAdvanced()) {
            var id = ItemDataHandler.getId(stack);
            id.ifPresent(uuid -> components.add(Component.literal(uuid.toString()).withStyle(ChatFormatting.GRAY)));
        }
    }}
