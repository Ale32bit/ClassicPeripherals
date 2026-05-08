package me.alexdevs.classicPeripherals.block;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import me.alexdevs.classicPeripherals.item.IDataItem;
import com.mojang.serialization.MapCodec;
import me.alexdevs.classicPeripherals.tiles.NfcReaderBlockEntity;
import me.alexdevs.classicPeripherals.utils.PocketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NfcReaderBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public enum NfcReaderState implements StringRepresentable {
        NONE("none"),
        READING("reading"),
        WRITING("writing"),
        SIGNING("signing"),
        ;

        public final String name;

        NfcReaderState(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static final EnumProperty<NfcReaderState> STATE = EnumProperty.create("state", NfcReaderState.class);

    public NfcReaderBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .setValue(STATE, NfcReaderState.NONE)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(NfcReaderBlock::new);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
                .add(HorizontalDirectionalBlock.FACING)
                .add(STATE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STATE, NfcReaderState.NONE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        var be = level.getBlockEntity(pos);
        if (be instanceof NfcReaderBlockEntity reader) {
            if (stack.getItem() instanceof IDataItem) {
                if (level.isClientSide) {
                    return ItemInteractionResult.SUCCESS;
                }

                reader.onUse(stack);
                return ItemInteractionResult.CONSUME;
            } else if (stack.is(ComputerCraftTags.Items.POCKET_COMPUTERS)) {
                if (level.isClientSide) {
                    return ItemInteractionResult.SUCCESS;
                }

                var pocket = (PocketServerComputer) PocketUtils.getServerComputer(level.getServer(), stack);
                if (pocket != null) {
                    reader.onPocketUse(pocket);
                    return ItemInteractionResult.CONSUME;
                }
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NfcReaderBlockEntity(pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, state.setValue(STATE, NfcReaderState.NONE));
    }
}
