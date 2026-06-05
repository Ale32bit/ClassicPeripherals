package me.alexdevs.classicPeripherals.platform.services;

import java.nio.file.Path;

/**
 * Platform information that differs between Fabric and NeoForge. Resolved at runtime through
 * {@link me.alexdevs.classicPeripherals.platform.Services}.
 */
public interface IPlatformHelper {

    /** Gets the name of the current platform. */
    String getPlatformName();

    /** Checks if a mod with the given id is loaded. */
    boolean isModLoaded(String modId);

    /** Whether the game is running in a development environment. */
    boolean isDevelopmentEnvironment();

    /** The loader's configuration directory, used to load the mod's TOML config. */
    Path getConfigDir();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
