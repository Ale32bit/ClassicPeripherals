package me.alexdevs.ccNetworks.item;

import me.alexdevs.ccNetworks.CcNetworks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final Item COPPER_COIL = register("copper_coil", new Item.Properties());

    public static Item register(String name, Item.Properties properties) {
        ResourceLocation id = new ResourceLocation(CcNetworks.MOD_ID, name);
        return Registry.register(BuiltInRegistries.ITEM, id, new Item(properties));
    }

    public static void initialize() {}
}
