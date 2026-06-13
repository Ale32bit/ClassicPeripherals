package me.alexdevs.classicPeripherals.registry;

import com.mojang.serialization.Codec;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponents;
import dan200.computercraft.api.upgrades.UpgradeType;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import me.alexdevs.classicPeripherals.platform.UpgradeRegistrar;
import me.alexdevs.classicPeripherals.registry.block.CryptographicAcceleratorBlock;
import me.alexdevs.classicPeripherals.registry.block.NfcReaderBlock;
import me.alexdevs.classicPeripherals.registry.block.RfidScannerBlock;
import me.alexdevs.classicPeripherals.registry.block.ScannerBlock;
import me.alexdevs.classicPeripherals.registry.block.antenna.AntennaBlock;
import me.alexdevs.classicPeripherals.registry.block.tower.TowerBaseBlock;
import me.alexdevs.classicPeripherals.registry.block.tower.TowerHeadBlock;
import me.alexdevs.classicPeripherals.registry.block.tower.TowerSegmentBlock;
import me.alexdevs.classicPeripherals.registry.item.NfcCardItem;
import me.alexdevs.classicPeripherals.registry.item.RfidBadgeItem;
import me.alexdevs.classicPeripherals.registry.luaApi.PocketNfcAPI;
import me.alexdevs.classicPeripherals.registry.recipe.NfcCardRecipe;
import me.alexdevs.classicPeripherals.registry.recipe.RfidBadgeRecipe;
import me.alexdevs.classicPeripherals.registry.screen.ScannerMenu;
import me.alexdevs.classicPeripherals.registry.tiles.*;
import me.alexdevs.classicPeripherals.registry.upgrades.crypto.PocketCrypto;
import me.alexdevs.classicPeripherals.registry.upgrades.crypto.TurtleCrypto;
import me.alexdevs.classicPeripherals.registry.upgrades.radio.PocketRadio;
import me.alexdevs.classicPeripherals.registry.upgrades.radio.TurtleRadio;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.UUID;
import java.util.function.Supplier;

public class ModRegistry {
    private static final Registrar<RecipeSerializer<?>> RECIPES = Services.REGISTRATION.create(Registries.RECIPE_SERIALIZER, ClassicPeripherals.MOD_ID);
    private static final Registrar<Block> BLOCKS = Services.REGISTRATION.create(Registries.BLOCK, ClassicPeripherals.MOD_ID);
    private static final Registrar<Item> ITEMS = Services.REGISTRATION.create(Registries.ITEM, ClassicPeripherals.MOD_ID);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = Services.REGISTRATION.create(Registries.BLOCK_ENTITY_TYPE, ClassicPeripherals.MOD_ID);
    private static final Registrar<MenuType<?>> MENUS = Services.REGISTRATION.create(Registries.MENU, ClassicPeripherals.MOD_ID);
    private static final Registrar<DataComponentType<?>> COMPONENTS = Services.REGISTRATION.create(Registries.DATA_COMPONENT_TYPE, ClassicPeripherals.MOD_ID);

    public static class Blocks {
        public static final RegistrySupplier<TowerBaseBlock> TOWER_BASE = register("tower_base", () -> new TowerBaseBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
        ));
        public static final RegistrySupplier<TowerSegmentBlock> TOWER_SEGMENT = register("tower_segment", () -> new TowerSegmentBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
        ));
        public static final RegistrySupplier<TowerHeadBlock> TOWER_HEAD = register("tower_head", () -> new TowerHeadBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.GOLD)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
        ));
        public static final RegistrySupplier<AntennaBlock> ANTENNA = register("antenna", () -> new AntennaBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.GOLD)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
        ));
        public static final RegistrySupplier<NfcReaderBlock> NFC_READER = register("nfc_reader", () -> new NfcReaderBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .isValidSpawn(Blocks::never)
        ));
        public static final RegistrySupplier<RfidScannerBlock> RFID_SCANNER = register("rfid_scanner", () -> new RfidScannerBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
        ));
        public static final RegistrySupplier<CryptographicAcceleratorBlock> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator", () -> new CryptographicAcceleratorBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
        ));
        public static final RegistrySupplier<ScannerBlock> SCANNER = register("scanner", () -> new ScannerBlock(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .mapColor(MapColor.STONE)
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
        ));

        private static <T extends Block> RegistrySupplier<T> register(String name, Supplier<T> block) {
            var registered = BLOCKS.register(name, block);
            ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
            return registered;
        }

        public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
            return false;
        }

        public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
            return false;
        }

        static void initialize() {
        }
    }

    public static class Items {
        public static final RegistrySupplier<Item> COPPER_COIL = register("copper_coil", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<NfcCardItem> NFC_CARD = register("nfc_card", () -> new NfcCardItem(new Item.Properties()
                .stacksTo(1)
        ));
        public static final RegistrySupplier<RfidBadgeItem> RFID_BADGE = register("rfid_badge", () -> new RfidBadgeItem(new Item.Properties()
                .stacksTo(1)
        ));

        public static <T extends Item> RegistrySupplier<T> register(String name, Supplier<T> item) {
            return ITEMS.register(name, item);
        }

        static void initialize() {
        }
    }

    public static class TileEntities {
        public static final RegistrySupplier<BlockEntityType<TowerBlockEntity>> TOWER_BASE = register("tower_base",
                () -> BlockEntityType.Builder.of(TowerBlockEntity::new, Blocks.TOWER_BASE.get()).build(null));
        public static final RegistrySupplier<BlockEntityType<AntennaBlockEntity>> ANTENNA = register("antenna",
                () -> BlockEntityType.Builder.of(AntennaBlockEntity::new, Blocks.ANTENNA.get()).build(null));
        public static final RegistrySupplier<BlockEntityType<NfcReaderBlockEntity>> NFC_READER = register("nfc_reader",
                () -> BlockEntityType.Builder.of(NfcReaderBlockEntity::new, Blocks.NFC_READER.get()).build(null));
        public static final RegistrySupplier<BlockEntityType<RfidScannerBlockEntity>> RFID_SCANNER = register("rfid_scanner",
                () -> BlockEntityType.Builder.of(RfidScannerBlockEntity::new, Blocks.RFID_SCANNER.get()).build(null));
        public static final RegistrySupplier<BlockEntityType<CryptographicAcceleratorBlockEntity>> CRYPTOGRAPHIC_ACCELERATOR = register("cryptographic_accelerator",
                () -> BlockEntityType.Builder.of(CryptographicAcceleratorBlockEntity::new, Blocks.CRYPTOGRAPHIC_ACCELERATOR.get()).build(null));
        public static final RegistrySupplier<BlockEntityType<ScannerBlockEntity>> SCANNER = register("scanner",
                () -> BlockEntityType.Builder.of(ScannerBlockEntity::new, Blocks.SCANNER.get()).build(null));

        private static <T extends BlockEntityType<?>> RegistrySupplier<T> register(String path, Supplier<T> factory) {
            return BLOCK_ENTITIES.register(path, factory);
        }

        static void initialize() {
        }
    }

    public static class Peripherals {
        static void initialize() {
        }
    }

    public static class Upgrades {
        public static final UpgradeType<PocketRadio> POCKET_RADIO = UpgradeType.simpleWithCustomItem(PocketRadio::new);
        public static final UpgradeType<TurtleRadio> TURTLE_RADIO = UpgradeType.simpleWithCustomItem(TurtleRadio::new);

        public static final UpgradeType<PocketCrypto> POCKET_CRYPTO = UpgradeType.simpleWithCustomItem(PocketCrypto::new);
        public static final UpgradeType<TurtleCrypto> TURTLE_CRYPTO = UpgradeType.simpleWithCustomItem(TurtleCrypto::new);

        public static void register(UpgradeRegistrar registrar) {
            registrar.registerPocketUpgrade("radio", POCKET_RADIO);
            registrar.registerTurtleUpgrade("radio", TURTLE_RADIO);
            registrar.registerPocketUpgrade("crypto", POCKET_CRYPTO);
            registrar.registerTurtleUpgrade("crypto", TURTLE_CRYPTO);
        }

        static void initialize() {
        }
    }

    public static class LuaApis {
        static void initialize() {
            ComputerCraftAPI.registerAPIFactory(computer -> {
                var pocket = computer.getComponent(ComputerComponents.POCKET);
                return pocket != null ? new PocketNfcAPI(pocket) : null;
            });
        }
    }

    public static class Recipes {
        public static final RegistrySupplier<SimpleCraftingRecipeSerializer<RfidBadgeRecipe>> RFID_BADGE_RECIPE =
                RECIPES.register("rfid_badge_dye",
                        () -> new SimpleCraftingRecipeSerializer<>(RfidBadgeRecipe::new));
        public static final RegistrySupplier<SimpleCraftingRecipeSerializer<NfcCardRecipe>> NFC_CARD_DYE =
                RECIPES.register("nfc_card_dye",
                        () -> new SimpleCraftingRecipeSerializer<>(NfcCardRecipe::new));

        static void initialize() {
        }
    }

    public static class DataComponents {
        public static final RegistrySupplier<DataComponentType<String>> DATAHOLDER_DATA = register("nfc_data",
                DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
        public static final RegistrySupplier<DataComponentType<Boolean>> DATAHOLDER_READONLY = register("nfc_readonly",
                DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        public static final RegistrySupplier<DataComponentType<Integer>> DATAHOLDER_COLOR = register("nfc_color",
                DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        public static final RegistrySupplier<DataComponentType<UUID>> DATAHOLDER_UUID = register("data_uuid",
                DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());

        private static <T> RegistrySupplier<DataComponentType<T>> register(String name, DataComponentType<T> type) {
            return COMPONENTS.register(name, () -> type);
        }

        static void initialize() {
        }
    }

    public static class Screens {
        public static final RegistrySupplier<MenuType<ScannerMenu>> SCANNER =
                MENUS.register("scanner",
                        () -> new MenuType<>(ScannerMenu::new, FeatureFlags.DEFAULT_FLAGS));

        static void initialize() {
        }
    }

    public static void initialize() {
        // We need to call a function (even if empty) inside the subclass to initialize the fields.
        DataComponents.initialize();
        Blocks.initialize();
        Items.initialize();
        TileEntities.initialize();
        Peripherals.initialize();
        Upgrades.initialize();
        LuaApis.initialize();
        Recipes.initialize();
        Screens.initialize();
    }
}
