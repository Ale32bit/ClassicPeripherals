package me.alexdevs.classicPeripherals.compat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FloatRange {
    double min();
    double max();
}
