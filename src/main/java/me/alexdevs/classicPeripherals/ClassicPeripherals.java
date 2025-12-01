package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.client.turtle.RegisterTurtleModellersEvent;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.compat.ConfigManager;
import me.alexdevs.classicPeripherals.item.AbstractDataItem;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.peripherals.Peripherals;
import me.alexdevs.classicPeripherals.recipe.ModRecipes;
import me.alexdevs.classicPeripherals.tiles.ModBlockTiles;
import me.alexdevs.classicPeripherals.upgrades.ModUpgrades;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod(ClassicPeripherals.MOD_ID)
public class ClassicPeripherals {
    public static final String MOD_ID = "classicperipherals";

    public static final ClassicPeripheralsConfig CONFIG = ConfigManager.createToml(
            FMLPaths.CONFIGDIR.get(),
            MOD_ID,
            ClassicPeripheralsConfig.class,
            ClassicPeripheralsConfig::new
    );

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS
            .register("classicperipherals",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.classicperipherals"))
                            .icon(() -> ModBlocks.TOWER_HEAD.get().asItem()
                                    .getDefaultInstance())
                            .displayItems((parameters, entries) -> {
                                entries.accept(ModBlocks.TOWER_BASE.get());
                                entries.accept(ModBlocks.TOWER_SEGMENT.get());
                                entries.accept(ModBlocks.TOWER_HEAD.get());
                                entries.accept(ModBlocks.ANTENNA.get());
                                entries.accept(ModItems.COPPER_COIL.get());
                                entries.accept(ModBlocks.NFC_READER.get());
                                entries.accept(ModItems.NFC_CARD.get());
                                entries.accept(ModBlocks.RFID_SCANNER.get());
                                entries.accept(ModItems.RFID_BADGE.get());
                            }).build());

    public ClassicPeripherals() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onDataGen);
        modEventBus.addListener(this::registerUpgrades);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModBlocks.initialize();
        ModItems.initialize();
        ModBlockTiles.initialize();
        ModRecipes.initialize();
        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, Peripherals::register);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    public void onDataGen(final GatherDataEvent event) {
        // use fabric's
        //ClassicPeripheralsDataGenerator.register(event);
    }

    private void registerUpgrades(RegisterEvent event) {
        event.register(
                PocketUpgradeSerialiser.registryId(),
                ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "radio"),
                () -> ModUpgrades.POCKET_RADIO
        );

        event.register(
                TurtleUpgradeSerialiser.registryId(),
                ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "radio"),
                () -> ModUpgrades.TURTLE_RADIO
        );
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        static void onItemColorHandler(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> {
                if (tintIndex == 0) {
                    return AbstractDataItem.getColor(stack);
                }

                return 0xFFFFFF;
            }, ModItems.NFC_CARD.get());

            event.register((stack, tintIndex) -> {
                if (tintIndex == 1) {
                    return AbstractDataItem.getColor(stack);
                }

                return 0xFFFFFF;
            }, ModItems.RFID_BADGE.get());
        }

        @SubscribeEvent
        public static void onUpgradeModeller(RegisterTurtleModellersEvent event) {
            event.register(ModUpgrades.TURTLE_RADIO, TurtleUpgradeModeller.sided(
                    model("turtle_radio_left"),
                    model("turtle_radio_right")
            ));
        }

        private static ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
        }

        private static ResourceLocation model(String path) {
            return id("block/" + path);
        }
    }
}
