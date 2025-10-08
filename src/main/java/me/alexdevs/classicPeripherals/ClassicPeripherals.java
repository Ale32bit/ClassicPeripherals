package me.alexdevs.classicPeripherals;

import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.peripherals.Peripherals;
import me.alexdevs.classicPeripherals.recipe.ModRecipes;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ClassicPeripherals.MOD_ID)
public class ClassicPeripherals {
    public static final String MOD_ID = "classicperipherals";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS
            .register("classicperipherals",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.classicperipherals"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> ModBlocks.TOWER_HEAD.asItem()
                                    .getDefaultInstance())
                            .displayItems((parameters, entries) -> {
                                entries.accept(ModBlocks.TOWER_BASE);
                                entries.accept(ModBlocks.TOWER_SEGMENT);
                                entries.accept(ModBlocks.TOWER_HEAD);
                                entries.accept(ModBlocks.ANTENNA);
                                entries.accept(ModItems.COPPER_COIL);
                                entries.accept(ModBlocks.NFC_READER);
                                entries.accept(ModItems.NFC_CARD);
                            }).build());

    public ClassicPeripherals(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onDataGen);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        COMPONENTS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModComponents.initialize();
        ModBlocks.initialize();
        ModItems.initialize();
        ModBlockTiles.initialize();
        ModRecipes.initialize();
        Peripherals.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    public void onDataGen(final GatherDataEvent event) {
        // use fabric's
        //ClassicPeripheralsDataGenerator.register(event);
    }
}
