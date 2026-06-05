package me.alexdevs.classicPeripherals.integrations;

import dev.emi.trinkets.api.TrinketsApi;
import me.alexdevs.classicPeripherals.platform.services.IEquipmentIntegration;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class FabricEquipmentIntegration implements IEquipmentIntegration {
    @Override
    public boolean isLoaded() {
        return FabricLoader.getInstance().isModLoaded("trinkets");
    }

    @Override
    public Optional<ItemStack> getEquippedStack(ServerPlayer player, Item item) {
        var trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isEmpty()) return Optional.empty();

        var component = trinkets.get();
        if (!component.isEquipped(item)) return Optional.empty();

        return component.getEquipped(item).stream()
                .map(pair -> pair.getB())
                .findFirst();
    }
}
