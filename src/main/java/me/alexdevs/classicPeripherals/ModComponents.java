package me.alexdevs.classicPeripherals;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.UUID;

public class ModComponents {
    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path);
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> DATAHOLDER_DATA = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_data",
                    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DATAHOLDER_READONLY = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_readonly",
                    builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> DATAHOLDER_COLOR = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_color",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> DATAHOLDER_UUID = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("data_uuid",
                    builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));

    public static void initialize() {
    }
}
