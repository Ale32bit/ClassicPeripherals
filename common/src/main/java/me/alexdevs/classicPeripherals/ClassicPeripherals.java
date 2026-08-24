package me.alexdevs.classicPeripherals;

import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.upgrades.UpgradeBase;
import dan200.computercraft.api.upgrades.UpgradeData;
import dan200.computercraft.core.util.Colour;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import dan200.computercraft.shared.turtle.items.TurtleItem;
import dan200.computercraft.shared.util.DataComponentUtil;
import me.alexdevs.classicPeripherals.core.StateSaverAndLoader;
import me.alexdevs.classicPeripherals.peripherals.nfc.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClassicPeripherals {
    public static final String MOD_ID = "classicperipherals";

    public static final ClassicPeripheralsConfig CONFIG = ClassicPeripheralsConfig.createToml(
            Services.PLATFORM.getConfigDir(),
            "",
            MOD_ID,
            ClassicPeripheralsConfig.class
    );

    private static final Registrar<CreativeModeTab> CREATIVE_TABS = Services.REGISTRATION.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("classicperipherals",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.classicperipherals"))
                    .icon(() -> ModRegistry.Blocks.TOWER_HEAD.get().asItem().getDefaultInstance())
                    .displayItems((context, entries) -> {
                        entries.accept(ModRegistry.Blocks.TOWER_BASE.get());
                        entries.accept(ModRegistry.Blocks.TOWER_SEGMENT.get());
                        entries.accept(ModRegistry.Blocks.TOWER_HEAD.get());
                        entries.accept(ModRegistry.Blocks.ANTENNA.get());
                        entries.accept(ModRegistry.Items.COPPER_COIL.get());
                        entries.accept(ModRegistry.Blocks.NFC_READER.get());
                        entries.accept(ModRegistry.Blocks.RFID_SCANNER.get());
                        entries.accept(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR.get());
                        entries.accept(ModRegistry.Blocks.CRYPTOGRAPHIC_ACCELERATOR_SLIM.get());
                        entries.accept(ModRegistry.Blocks.SCANNER.get());

                        addTurtle(entries, dan200.computercraft.shared.ModRegistry.Items.TURTLE_NORMAL.get(), context.holders());
                        addTurtle(entries, dan200.computercraft.shared.ModRegistry.Items.TURTLE_ADVANCED.get(), context.holders());
                        addPocket(entries, dan200.computercraft.shared.ModRegistry.Items.POCKET_COMPUTER_NORMAL.get(), context.holders());
                        addPocket(entries, dan200.computercraft.shared.ModRegistry.Items.POCKET_COMPUTER_ADVANCED.get(), context.holders());

                        addColoredItem(entries, ModRegistry.Items.NFC_CARD.get());
                        addColoredItem(entries, ModRegistry.Items.RFID_BADGE.get());
                    })
                    .build());

    private static @Nullable StateSaverAndLoader stateSaverAndLoader;

    public static void init() {
        ModRegistry.initialize();
    }

    public static void onServerStarted(MinecraftServer server) {
        PocketNfcAccess.clear();
        stateSaverAndLoader = StateSaverAndLoader.getServerState(server);
    }

    public static @Nullable StateSaverAndLoader getState() {
        return stateSaverAndLoader;
    }

    public static void onTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> lines) {
        if (stack.is(dan200.computercraft.shared.ModRegistry.Blocks.WIRELESS_MODEM_ADVANCED.get().asItem())) {
            if (CONFIG.enderModemNerf) {
                lines.add(Component.translatable("tooltip.classicperipherals.enderModemNerf", ClassicPeripherals.CONFIG.enderModemRangeMultiplier).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static void addTurtle(CreativeModeTab.Output out, TurtleItem turtle, HolderLookup.Provider registries) {
        registries.lookupOrThrow(ITurtleUpgrade.REGISTRY).listElements()
                .filter(ClassicPeripherals::isOurUpgrade)
                .map(x -> DataComponentUtil.createStack(turtle, dan200.computercraft.shared.ModRegistry.DataComponents.RIGHT_TURTLE_UPGRADE.get(), UpgradeData.ofDefault(x)))
                .forEach(out::accept);
    }

    private static void addPocket(CreativeModeTab.Output out, PocketComputerItem pocket, HolderLookup.Provider registries) {
        registries.lookupOrThrow(IPocketUpgrade.REGISTRY).listElements()
                .filter(ClassicPeripherals::isOurUpgrade)
                .map(x -> DataComponentUtil.createStack(pocket, dan200.computercraft.shared.ModRegistry.DataComponents.POCKET_UPGRADE.get(), UpgradeData.ofDefault(x))).forEach(out::accept);
    }

    private static boolean isOurUpgrade(Holder.Reference<? extends UpgradeBase> upgrade) {
        var namespace = upgrade.key().location().getNamespace();
        return namespace.equals(MOD_ID);
    }

    private static void addColoredItem(CreativeModeTab.Output out, Item item) {
        for (var color : Colour.VALUES) {
            out.accept(DataComponentUtil.createStack(
                    item, ModRegistry.DataComponents.DATAHOLDER_COLOR.get(), color.getHex()
            ));
        }
    }
}
