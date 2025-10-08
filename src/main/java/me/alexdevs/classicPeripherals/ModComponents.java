package me.alexdevs.classicPeripherals;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;


public class ModComponents {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RADIO_CHANNEL = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("radio_channel",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> NFC_DATA = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_data",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NFC_READONLY = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_readonly",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> NFC_COLOR = ClassicPeripherals
            .COMPONENTS
            .registerComponentType("nfc_color",
            builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static void initialize() {
    }
}
