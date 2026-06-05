package me.alexdevs.classicPeripherals.platform;

import me.alexdevs.classicPeripherals.platform.services.IEquipmentIntegration;
import me.alexdevs.classicPeripherals.platform.services.IPlatformHelper;
import me.alexdevs.classicPeripherals.platform.services.IRegistrationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * Loads the platform-specific service implementations via {@link ServiceLoader}. Each loader module
 * ships {@code META-INF/services} files naming its implementation classes.
 */
public final class Services {
    private static final Logger LOG = LoggerFactory.getLogger("classicperipherals/Services");

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistrationFactory REGISTRATION = load(IRegistrationFactory.class);
    public static final IEquipmentIntegration EQUIPMENT = load(IEquipmentIntegration.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    private Services() {
    }
}
