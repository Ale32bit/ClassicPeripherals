package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RfidBadgeItem extends AbstractDataItem {
    public RfidBadgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (interactionTarget instanceof Player) {
            return InteractionResult.PASS;
        }

        var data = AbstractDataItem.getData(stack);
        if (data.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        var entity = (ILivingEntityMixin) interactionTarget;
        entity.setRfidData(data.get());

        if(!player.getAbilities().instabuild) {
            // stack.shrink wouldn't work :/
            player.getItemInHand(usedHand).shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
