package me.alexdevs.classicPeripherals;

import me.alexdevs.classicPeripherals.core.StateSaverAndLoader;
import me.alexdevs.classicPeripherals.network.S2CConfigurationPayload;
import me.alexdevs.classicPeripherals.peripherals.nfc.luaApi.PocketNfcAccess;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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

    public static ServerConfig SERVER_CONFIG = new ServerConfig(CONFIG);

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
            if (SERVER_CONFIG.enderModemNerf()) {
                lines.add(Component.translatable("tooltip.classicperipherals.enderModemNerf", SERVER_CONFIG.enderModemRangeMultiplier()).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static S2CConfigurationPayload getConfigurationPayload() {
        return new S2CConfigurationPayload(CONFIG.enderModemNerf, CONFIG.enderModemRangeMultiplier);
    }

    public static void applyConfiguration(S2CConfigurationPayload payload) {
        SERVER_CONFIG = new ServerConfig(payload.enderModemNerf(), payload.enderModemRangeMultiplier());
    }
}
