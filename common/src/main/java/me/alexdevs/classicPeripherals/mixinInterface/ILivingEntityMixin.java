package me.alexdevs.classicPeripherals.mixinInterface;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ILivingEntityMixin {
    Optional<String> classicPeripherals$getRfidData();

    void classicPeripherals$setRfidData(@Nullable String data);

    boolean classicPeripherals$hasRfidData();
}
