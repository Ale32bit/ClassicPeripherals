package me.alexdevs.classicPeripherals.integrations;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class SableIntegration {
    public static double getDistanceSquared(Level level, Vec3 pos1, Vec3 pos2) {
        var a = toVector(pos1);
        var b = toVector(pos2);
        var distance = SableCompanion.INSTANCE.distanceSquaredWithSubLevels(level, a, b);

        return distance;
    }

    public static Vector3d toVector(Vec3 vec3) {
        return new Vector3d(vec3.x, vec3.y, vec3.z);
    }
}
