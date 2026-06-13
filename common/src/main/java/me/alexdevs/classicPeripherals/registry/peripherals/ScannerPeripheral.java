package me.alexdevs.classicPeripherals.registry.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.ObjectLuaTable;
import dan200.computercraft.api.peripheral.AttachedComputerSet;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.media.items.PrintoutData;
import dan200.computercraft.shared.media.items.PrintoutItem;
import me.alexdevs.classicPeripherals.registry.tiles.ScannerBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannerPeripheral implements IPeripheral {
    private final ScannerBlockEntity scanner;
    private final AttachedComputerSet computers = new AttachedComputerSet();

    public ScannerPeripheral(ScannerBlockEntity scanner) {
        this.scanner = scanner;
    }

    @Override
    public @NonNull String getType() {
        return "scanner";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof ScannerPeripheral o && scanner == o.scanner;
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@NonNull IComputerAccess computer) {
        computers.remove(computer);
    }

    private static boolean isValid(ItemStack stack) {
        return stack.getItem() instanceof PrintoutItem;
    }

    public void emitScannerEvent(boolean inserted) {
        var eventName = inserted ? "scanner" : "scanner_eject";
        computers.forEach(computer -> computer.queueEvent(eventName, computer.getAttachmentName()));
    }

    @LuaFunction
    public final boolean hasPages() {
        return isValid(scanner.getPrintout());
    }

    @LuaFunction
    public final String getLabel() {
        var stack = scanner.getPrintout();
        if (!isValid(stack)) {
            return null;
        }

        var data = PrintoutData.getOrEmpty(stack);

        var title = data.getTitle();
        return title.isEmpty() ? null : title;
    }

    @LuaFunction
    public final int getPageCount() {
        var stack = scanner.getPrintout();
        if (!isValid(stack)) {
            return 0;
        }

        var data = PrintoutData.getOrEmpty(stack);

        return data.pages();
    }

    @LuaFunction
    public final void eject() {
        scanner.eject();
    }

    @LuaFunction
    public final ObjectLuaTable scan() {
        var printout = scanner.getPrintout();
        if (!isValid(printout)) {
            return null;
        }

        var data = PrintoutData.getOrEmpty(printout);

        var pageCount = data.pages();
        var lines = data.lines();

        // pages[
        //      page[
        //          text[], colors[]
        //      ]
        // ]
        return extractPages(pageCount, lines);
    }

    private static @NonNull ObjectLuaTable extractPages(int pageCount, List<PrintoutData.Line> lines) {
        var pages = new HashMap<Integer, Map<Integer, Map<Integer, String>>>();

        for (var i = 0; i < pageCount; i++) {
            var page = new HashMap<Integer, Map<Integer, String>>();
            var texts = new HashMap<Integer, String>();
            var colors = new HashMap<Integer, String>();
            page.put(1, texts);
            page.put(2, colors);
            pages.put(i + 1, page);

            for (var j = i * PrintoutData.LINES_PER_PAGE; j < (i + 1) * PrintoutData.LINES_PER_PAGE; j++) {
                texts.put(j + 1, lines.get(j).text());
                colors.put(j + 1, lines.get(j).foreground());
            }
        }

        return new ObjectLuaTable(pages);
    }
}
