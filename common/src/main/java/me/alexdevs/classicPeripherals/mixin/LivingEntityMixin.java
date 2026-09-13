package me.alexdevs.classicPeripherals.mixin;

import me.alexdevs.classicPeripherals.mixinInterface.ILivingEntityMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityMixin {
    @Unique
    private static final String RFID_DATA_KEY = "rfidBadgeData";
    @Unique
    private String classicPeripherals$rfidBadgeData = null;

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void classicPeripherals$addData(CompoundTag compound, CallbackInfo ci) {
        if (classicPeripherals$rfidBadgeData != null) {
            compound.putString(RFID_DATA_KEY, classicPeripherals$rfidBadgeData);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void classicPeripherals$readData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(RFID_DATA_KEY, Tag.TAG_STRING)) {
            classicPeripherals$rfidBadgeData = compound.getString("rfidBadgeData");
        }
    }

    @Override
    public Optional<String> classicPeripherals$getRfidData() {
        return Optional.ofNullable(classicPeripherals$rfidBadgeData);
    }

    @Override
    public void classicPeripherals$setRfidData(@Nullable String data) {
        classicPeripherals$rfidBadgeData = data;
    }

    @Override
    public boolean classicPeripherals$hasRfidData() {
        return classicPeripherals$rfidBadgeData != null;
    }
}
