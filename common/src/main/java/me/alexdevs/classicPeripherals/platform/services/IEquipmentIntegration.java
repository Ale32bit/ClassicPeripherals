package me.alexdevs.classicPeripherals.platform.services;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * Abstraction over the loader's equipment/accessory mod (Trinkets on Fabric, Curios on NeoForge),
 * used to locate an equipped RFID badge on a player. Implementations return empty when the
 * accessory mod is absent.
 */
public interface IEquipmentIntegration {

    /** Whether the backing accessory mod (Trinkets/Curios) is present. */
    boolean isLoaded();

    /** Finds the first stack equipped in an accessory slot whose item matches {@code item}. */
    Optional<ItemStack> getEquippedStack(ServerPlayer player, Item item);
}
