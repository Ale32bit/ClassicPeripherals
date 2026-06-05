package me.alexdevs.classicPeripherals;

import com.mojang.serialization.Codec;
import me.alexdevs.classicPeripherals.platform.Registrar;
import me.alexdevs.classicPeripherals.platform.RegistrySupplier;
import me.alexdevs.classicPeripherals.platform.Services;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.UUID;

public class ModComponents {
    private static final Registrar<DataComponentType<?>> COMPONENTS = Services.REGISTRATION.create(Registries.DATA_COMPONENT_TYPE, ClassicPeripherals.MOD_ID);

    @SuppressWarnings("unchecked")
    private static <T> RegistrySupplier<DataComponentType<T>> register(String name, DataComponentType<T> type) {
        return (RegistrySupplier<DataComponentType<T>>) (RegistrySupplier<?>) COMPONENTS.register(name, () -> type);
    }

    public static final RegistrySupplier<DataComponentType<String>> DATAHOLDER_DATA = register("nfc_data",
            DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    public static final RegistrySupplier<DataComponentType<Boolean>> DATAHOLDER_READONLY = register("nfc_readonly",
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());

    public static final RegistrySupplier<DataComponentType<Integer>> DATAHOLDER_COLOR = register("nfc_color",
            DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

    public static final RegistrySupplier<DataComponentType<UUID>> DATAHOLDER_UUID = register("data_uuid",
            DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());

    public static void initialize() {
    }
}
