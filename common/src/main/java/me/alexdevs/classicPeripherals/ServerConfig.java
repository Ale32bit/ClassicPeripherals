package me.alexdevs.classicPeripherals;

/// Configuration that the server sends to the client for informational purposes.
/// This is to avoid overriding the mod configuration and messing with local server worlds after joining dedicated servers.
///
/// @param enderModemNerf
/// @param enderModemRangeMultiplier
public record ServerConfig(boolean enderModemNerf, double enderModemRangeMultiplier) {
    public ServerConfig(ClassicPeripheralsConfig config) {
        this(config.enderModemNerf, config.enderModemRangeMultiplier);
    }
}
