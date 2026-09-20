package me.alexdevs.classicPeripherals.network;

import me.alexdevs.classicPeripherals.ClassicPeripherals;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2CConfigurationPayload(boolean enderModemNerf, double enderModemRangeMultiplier) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, "configuration");

    public static final CustomPacketPayload.Type<S2CConfigurationPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, S2CConfigurationPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            S2CConfigurationPayload::enderModemNerf,
            ByteBufCodecs.DOUBLE,
            S2CConfigurationPayload::enderModemRangeMultiplier,
            S2CConfigurationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
