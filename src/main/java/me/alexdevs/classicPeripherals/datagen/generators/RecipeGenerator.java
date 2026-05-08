package me.alexdevs.classicPeripherals.datagen.generators;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.ModRegistry;
import me.alexdevs.classicPeripherals.block.ModBlocks;
import me.alexdevs.classicPeripherals.item.ModItems;
import me.alexdevs.classicPeripherals.recipe.NfcCardRecipe;
import me.alexdevs.classicPeripherals.recipe.RfidBadgeRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends FabricRecipeProvider {

    public RecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.TOWER_BASE)
                .pattern("ici")
                .pattern("iai")
                .pattern("ImI")
                .define('i', Items.IRON_INGOT)
                .define('c', ModRegistry.Items.CABLE.get())
                .define('a', Items.AMETHYST_SHARD)
                .define('I', Items.IRON_BLOCK)
                .define('m', ComputerCraftTags.Items.WIRED_MODEM)
                .unlockedBy("has_modem", has(ComputerCraftTags.Items.WIRED_MODEM))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.TOWER_SEGMENT)
                .pattern("ici")
                .pattern("ici")
                .pattern("ici")
                .define('i', Items.IRON_BARS)
                .define('c', ModRegistry.Items.CABLE.get())
                .unlockedBy("has_tower_base", has(ModBlocks.TOWER_BASE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.TOWER_HEAD)
                .pattern("cmc")
                .pattern("msm")
                .pattern("cmc")
                .define('s', ModBlocks.TOWER_SEGMENT)
                .define('m', ModRegistry.Items.WIRELESS_MODEM_ADVANCED.get())
                .define('c', ModItems.COPPER_COIL)
                .unlockedBy("has_tower_segment", has(ModBlocks.TOWER_SEGMENT))
                .unlockedBy("has_copper_coil", has(ModItems.COPPER_COIL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.ANTENNA)
                .pattern(" m ")
                .pattern("mcm")
                .pattern(" M ")
                .define('m', ModRegistry.Items.WIRELESS_MODEM_NORMAL.get())
                .define('c', ModRegistry.Items.CABLE.get())
                .define('M', ComputerCraftTags.Items.WIRED_MODEM)
                .unlockedBy("has_modem", has(ComputerCraftTags.Items.WIRED_MODEM))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.COPPER_COIL, 2)
                .pattern("ccc")
                .pattern("cwc")
                .pattern("ccc")
                .define('w', ItemTags.PLANKS)
                .define('c', Items.COPPER_INGOT)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.NFC_READER)
                .pattern("sss")
                .pattern("srs")
                .pattern("scs")
                .define('s', Items.STONE)
                .define('r', Items.REDSTONE)
                .define('c', ModItems.COPPER_COIL)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModItems.NFC_CARD)
                .requires(Items.PAPER)
                .requires(Items.REDSTONE)
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        SpecialRecipeBuilder.special(NfcCardRecipe::new)
                .save(output, "nfc_card_dye");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.RFID_BADGE)
                .pattern(" n ")
                .pattern("ncn")
                .pattern(" n ")
                .define('n', Items.IRON_NUGGET)
                .define('c', ModItems.COPPER_COIL)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.RFID_SCANNER)
                .pattern("sss")
                .pattern("scs")
                .pattern("sss")
                .define('s', Items.STONE)
                .define('c', ModItems.COPPER_COIL)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(output);

        SpecialRecipeBuilder.special(RfidBadgeRecipe::new)
                .save(output, "rfid_badge_dye");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CRYPTOGRAPHIC_ACCELERATOR)
                .pattern("did")
                .pattern("cac")
                .pattern("iri")
                .define('d', Items.DIAMOND)
                .define('i', Items.IRON_INGOT)
                .define('c', Items.COMPARATOR)
                .define('a', ModRegistry.Items.COMPUTER_ADVANCED.get())
                .define('r', Items.REDSTONE_TORCH)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .unlockedBy("has_advanced_computer", has(ModRegistry.Items.COMPUTER_ADVANCED.get()))
                .unlockedBy("has_comparator", has(Items.COMPARATOR))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SCANNER)
                .pattern("sss")
                .pattern("sos")
                .pattern("srs")
                .define('s', Items.STONE)
                .define('o', Items.OBSERVER)
                .define('r', Items.REDSTONE)
                .unlockedBy("has_observer", has(Items.OBSERVER))
                .save(output);
    }
}
