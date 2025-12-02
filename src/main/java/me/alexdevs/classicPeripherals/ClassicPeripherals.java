package me.alexdevs.classicPeripherals;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.luaApi.ModLuaApiProvider;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.peripherals.Peripherals;
import me.alexdevs.classicPeripherals.recipe.ModRecipes;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ClassicPeripherals implements ModInitializer {
    public static final String MOD_ID = "classicperipherals";
    public static final ClassicPeripheralsConfig CONFIG = ClassicPeripheralsConfig.createToml(
            FabricLoader.getInstance().getConfigDir(),
            "",
            MOD_ID,
            ClassicPeripheralsConfig.class
    );

    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MOD_ID, "item_group"));
    public static final CreativeModeTab CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.TOWER_HEAD))
            .title(Component.translatable("itemGroup.classicperipherals"))
            .build();

    @Override
    public void onInitialize() {
        ModComponents.initialize();
        ModBlocks.initialize();
        ModBlockTiles.initialize();
        ModItems.initialize();
        Peripherals.register();
        ModRecipes.initialize();
        ModUpgrades.initialize();
        ModLuaApiProvider.initialize();

        ServerLifecycleEvents.SERVER_STARTED.register(this::serverStartHook);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            if (tab == CREATIVE_TAB) {
                entries.accept(ModBlocks.TOWER_BASE);
                entries.accept(ModBlocks.TOWER_SEGMENT);
                entries.accept(ModBlocks.TOWER_HEAD);
                entries.accept(ModBlocks.ANTENNA);
                entries.accept(ModItems.COPPER_COIL);
                entries.accept(ModBlocks.NFC_READER);
                entries.accept(ModItems.NFC_CARD);
                entries.accept(ModBlocks.RFID_SCANNER);
                entries.accept(ModItems.RFID_BADGE);
            }
        });
    }

    private void serverStartHook(MinecraftServer server) {
        PocketNfcAccess.clear();
    }
}
