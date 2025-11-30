package me.alexdevs.classicPeripherals.item;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NfcCardItem extends AbstractDataItem {
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
            var computers = getAllPocketComputers(player);

            computers.forEach(computer -> computer.queueEvent("nfc_data", new Object[]{"internal", data.get()}));
        }

        return InteractionResult.CONSUME;
    }

    private static List<ServerComputer> getAllPocketComputers(ServerPlayer player) {
        var list = new ArrayList<ServerComputer>();
        var inventory = player.getInventory();
        for (var i = 0; i < inventory.getContainerSize(); i++) {
            var item = inventory.getItem(i);
            if (item.getTags().anyMatch(x -> x == ComputerCraftTags.Items.POCKET_COMPUTERS)) {
                var computer = PocketComputerItem.getServerComputer(player.getServer(), item);
                list.add(computer);
            }
        }

        return list;
    }
}
