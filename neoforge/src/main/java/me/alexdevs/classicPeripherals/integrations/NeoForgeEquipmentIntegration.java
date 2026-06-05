package me.alexdevs.classicPeripherals.integrations;

import me.alexdevs.classicPeripherals.platform.services.IEquipmentIntegration;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

public class NeoForgeEquipmentIntegration implements IEquipmentIntegration {
    @Override
    public boolean isLoaded() {
        return ModList.get().isLoaded("curios");
    }

    @Override
    public Optional<ItemStack> getEquippedStack(ServerPlayer player, Item item) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inv -> inv.findFirstCurio(stack -> stack.is(item)))
                .map(result -> result.stack());
    }
}
