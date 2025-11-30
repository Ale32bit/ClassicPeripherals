package me.alexdevs.classicPeripherals.mixinInterface;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ILivingEntityMixin {
    Optional<String> getRfidData();

    void setRfidData(@Nullable String data);

    boolean hasRfidData();
}
