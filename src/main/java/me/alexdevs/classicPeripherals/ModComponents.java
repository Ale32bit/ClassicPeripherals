package me.alexdevs.classicPeripherals;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class ModComponents {
    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }

    public static final DataComponentType<String> DATAHOLDER_DATA = register("nfc_data",
            builder -> builder.persistent(Codec.STRING));

    public static final DataComponentType<Boolean> DATAHOLDER_READONLY = register("nfc_readonly",
            builder -> builder.persistent(Codec.BOOL));

    public static final DataComponentType<Integer> DATAHOLDER_COLOR = register("nfc_color",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DataComponentType<String> DATAHOLDER_PRIVATEKEY = register("nfc_privatekey",
            builder -> builder.persistent(Codec.STRING));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), type);
    }

    public static void initialize() {
    }
}
