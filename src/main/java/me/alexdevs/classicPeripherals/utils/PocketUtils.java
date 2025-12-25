package me.alexdevs.classicPeripherals.utils;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PocketUtils {
    public static final String NBT_SESSION = "SessionId";

    public static List<ServerComputer> getAllPocketComputers(ServerPlayer player) {
        var list = new ArrayList<ServerComputer>();
        var inventory = player.getInventory();
        for (var i = 0; i < inventory.getContainerSize(); i++) {
            var item = inventory.getItem(i);
            if (item.getTags().anyMatch(x -> x == ComputerCraftTags.Items.POCKET_COMPUTERS)) {
                var computer = getServerComputer(player.getServer(), item);
                list.add(computer);
            }
        }

        return list;
    }

    public static ServerComputer getServerComputer(MinecraftServer server, ItemStack stack) {
        var registry = ServerContext.get(server).registry();
        return registry.get(getSessionID(stack), getInstanceID(stack));
    }

    public static UUID getInstanceID(ItemStack stack) {
        return PocketComputerItem.getInstanceID(stack);
    }

    public static int getSessionID(ItemStack stack) {
        var nbt = stack.getTag();
        return nbt != null && nbt.contains(NBT_SESSION) ? nbt.getInt(NBT_SESSION) : -1;
    }
}
