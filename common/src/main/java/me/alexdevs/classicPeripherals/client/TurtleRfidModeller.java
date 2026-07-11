package me.alexdevs.classicPeripherals.client;

import dan200.computercraft.api.client.ModelLocation;
import dan200.computercraft.api.client.TransformedModel;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.shared.util.DataComponentUtil;
import me.alexdevs.classicPeripherals.ClassicPeripherals;
import me.alexdevs.classicPeripherals.ModRegistry;
import me.alexdevs.classicPeripherals.peripherals.rfid.upgrades.TurtleRfid;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

import java.util.stream.Stream;

public class TurtleRfidModeller implements TurtleUpgradeModeller<TurtleRfid> {
    private final ModelLocation leftOffModel = model("block/turtle_rfid_off_left");
    private final ModelLocation rightOffModel = model("block/turtle_rfid_off_right");
    private final ModelLocation leftOnModel = model("block/turtle_rfid_on_left");
    private final ModelLocation rightOnModel = model("block/turtle_rfid_on_right");

    @Override
    public TransformedModel getModel(TurtleRfid upgrade, @Nullable ITurtleAccess turtle, TurtleSide side, DataComponentPatch data) {
        var active = DataComponentUtil.isPresent(data, ModRegistry.DataComponents.ON.get(), x -> x);

        return side == TurtleSide.LEFT
                ? TransformedModel.of(active ? leftOnModel : leftOffModel)
                : TransformedModel.of(active ? rightOnModel : rightOffModel);
    }

    @Override
    public Stream<ResourceLocation> getDependencies() {
        return Stream.of(leftOffModel, rightOffModel, leftOnModel, rightOnModel).flatMap(ModelLocation::getDependencies);
    }

    private static ModelLocation model(String path) {
        return ModelLocation.ofResource(ResourceLocation.fromNamespaceAndPath(ClassicPeripherals.MOD_ID, path));
    }
}