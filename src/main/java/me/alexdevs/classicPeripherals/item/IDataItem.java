package me.alexdevs.classicPeripherals.item;

import me.alexdevs.classicPeripherals.ModComponents;
import me.alexdevs.classicPeripherals.core.ItemDataHandler;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IDataItem {
    boolean supportsPrivateKey();
}
