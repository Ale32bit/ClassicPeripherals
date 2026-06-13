package me.alexdevs.classicPeripherals.registry.block;

import dan200.computercraft.api.ComputerCraftTags;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import com.mojang.serialization.MapCodec;
import me.alexdevs.classicPeripherals.registry.item.IDataItem;
import me.alexdevs.classicPeripherals.registry.tiles.NfcReaderBlockEntity;
import me.alexdevs.classicPeripherals.utils.PocketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import org.jspecify.annotations.NonNull;

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
    protected @NonNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(NfcReaderBlock::new);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NonNull BlockState state) {
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
    protected @NonNull ItemInteractionResult useItemOn(ItemStack stack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
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
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new NfcReaderBlockEntity(pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        level.setBlockAndUpdate(pos, state.setValue(STATE, NfcReaderState.NONE));
    }
}
