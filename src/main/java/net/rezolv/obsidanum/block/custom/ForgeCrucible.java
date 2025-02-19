package net.rezolv.obsidanum.block.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.forge_crucible.update_ingredients.UpdateIngredientsForgeCrucible;
import net.rezolv.obsidanum.block.forge_crucible.neigbor_changed.AddTagsForgeCrucible;
import org.jetbrains.annotations.Nullable;

import java.util.ListIterator;


public class ForgeCrucible extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public ForgeCrucible(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ForgeCrucibleEntity crucible)) {
            return InteractionResult.PASS;
        }

        ItemStack heldStack = player.getItemInHand(hand);

        // Добавление предмета
        if (!heldStack.isEmpty()) {
            return handleItemDeposit(level, player, heldStack, crucible,pos,state);
        }
        // Извлечение предметов
        else if (player.isShiftKeyDown()) {
            return handleItemWithdrawal(level,player, crucible,pos,state);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleItemDeposit(Level level, Player player, ItemStack stack, ForgeCrucibleEntity crucible, BlockPos pos, BlockState state) {
        // Добавим отладочный вывод
        Obsidanum.LOGGER.info("Trying to deposit: {}", stack);

        CompoundTag data = crucible.getReceivedData();
        if (!data.contains("Ingredients", Tag.TAG_LIST)) {
            Obsidanum.LOGGER.warn("No ingredients found in crucible data");
            return InteractionResult.PASS;
        }

        ListTag ingredients = data.getList("Ingredients", Tag.TAG_COMPOUND);
        ItemStack stackCopy = stack.copyWithCount(1);

        for (int i = 0; i < ingredients.size(); i++) {
            CompoundTag ingTag = ingredients.getCompound(i);
            try {
                JsonObject json = JsonParser.parseString(ingTag.getString("IngredientJson")).getAsJsonObject();
                Ingredient ingredient = Ingredient.fromJson(json);

                // Добавим проверку тегов
                if (ingredient.test(stackCopy)) {
                    int required = json.get("count").getAsInt();
                    int current = (int) crucible.depositedItems.stream()
                            .filter(s -> ItemStack.isSameItemSameTags(s, stackCopy))
                            .count();

                    if (current < required) {
                        ItemStack deposited = stackCopy.copy();
                        crucible.depositedItems.add(deposited);
                        stack.shrink(1);
                        crucible.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        Obsidanum.LOGGER.info("Deposited successfully. Current count: {}/{}", current+1, required);
                        crucible.debugDepositedItems("deposit");
                        return InteractionResult.SUCCESS;
                    }
                }
            } catch (Exception e) {
                Obsidanum.LOGGER.error("Error parsing ingredient: {}", e.getMessage());
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult handleItemWithdrawal(Level level,Player player, ForgeCrucibleEntity crucible, BlockPos pos, BlockState state) {
        if (crucible.depositedItems.isEmpty()) return InteractionResult.PASS;

        // Извлекаем в обратном порядке (последние добавленные первыми)
        ListIterator<ItemStack> iterator = crucible.depositedItems.listIterator(crucible.depositedItems.size());

        while (iterator.hasPrevious()) {
            ItemStack stack = iterator.previous();

            if (!stack.isEmpty()) {
                giveOrDropItem(player, stack.copy());
                iterator.remove();
                crucible.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private void giveOrDropItem(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        AddTagsForgeCrucible.handleNeighborUpdate(state, level, pos, fromPos);
    }
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgeCrucibleEntity(pPos, pState);
    }
}