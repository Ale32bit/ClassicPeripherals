package me.alexdevs.classicPeripherals;


import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;

public class ClassicPeripheralsConfig extends WrappedConfig {
    @Comment("The maximum height of the radio tower. Including base and head.")
    @IntegerRange(min = 2, max = Integer.MAX_VALUE)
    public int radioTowerMaxHeight = 24;

    @Comment("The minimum height of the radio tower. Including base and head.")
    @IntegerRange(min = 2, max = Integer.MAX_VALUE)
    public int radioTowerMinHeight = 2;

    @Comment("Range per segment.")
    @IntegerRange(min = 1, max = Integer.MAX_VALUE)
    public int radioTowerSegmentRange = 128;

    @Comment("The loss factor of the radio tower.")
    @FloatRange(min = 0d, max = 1d)
    public double radioTowerLossFactor = 0.15;

    @Comment("The maximum message size in bytes.")
    @IntegerRange(min = 1, max = Integer.MAX_VALUE)
    public int radioTowerMaxMessageSize = 8 * 1024 * 1024;

    @Comment("Whether the Mini Antenna can broadcast instead of just receiving.")
    public boolean antennaCanBroadcast = true;

    @Comment("The maximum size in bytes of the NFC card and RFID badge data.")
    @IntegerRange(min = 1, max = Integer.MAX_VALUE)
    public int nfcMaxDataSize = 128;

    @Comment("The maximum range of the RFID scanner.")
    @IntegerRange(min = 1, max = Integer.MAX_VALUE)
    public int rfidScanRange = 8;

    @Comment("Enable the Ender Modem nerf.")
    public boolean enderModemNerf = true;

    @Comment("Ender Modem cross-dimensional range.")
    @FloatRange(min = 1, max = Float.MAX_VALUE)
    public double enderModemCrossDimensionalRange = 8;

    @Comment("Enable the improvement of the Redstone Relay event.")
    public boolean betterRedstoneRelayEvent = true;
}
