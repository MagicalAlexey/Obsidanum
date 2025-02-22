package net.rezolv.obsidanum.block.custom;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;
import net.rezolv.obsidanum.block.enum_blocks.ScrollType;
import net.rezolv.obsidanum.block.forge_crucible.update_ingredients.UpdateIngredientsForgeCrucible;
import net.rezolv.obsidanum.block.forge_crucible.neigbor_changed.AddTagsForgeCrucible;
import org.jetbrains.annotations.Nullable;


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
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ForgeCrucibleEntity crucible)) {
            return InteractionResult.PASS;
        }
        return UpdateIngredientsForgeCrucible.handleInteraction(
                level,
                player,
                player.getItemInHand(hand),
                crucible,
                pos,
                state
        );
    }
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        AddTagsForgeCrucible.handleNeighborUpdate(state, level, pos, fromPos);

        Direction facing = state.getValue(ForgeCrucible.FACING);
        BlockPos expectedRightPos = switch (facing) {
            case NORTH -> pos.east();
            case SOUTH -> pos.west();
            case EAST -> pos.south();
            case WEST -> pos.north();
            default -> null;
        };

        if (expectedRightPos != null && expectedRightPos.equals(fromPos)) {
            BlockState leftBlockState = level.getBlockState(expectedRightPos);
            BlockEntity crucibleEntity = level.getBlockEntity(pos);

            if (leftBlockState.getBlock() instanceof LeftCornerLevel
                    && leftBlockState.hasProperty(LeftCornerLevel.IS_PRESSED)) {

                boolean isPressed = leftBlockState.getValue(LeftCornerLevel.IS_PRESSED);
                if (isPressed && crucibleEntity instanceof ForgeCrucibleEntity crucible) {
                    // Вызываем метод для обработки результата
                    handleResult(level, pos, crucible);
                }
            }
        }
    }

    private void handleResult(Level level, BlockPos pos, ForgeCrucibleEntity crucible) {
        CompoundTag data = crucible.getReceivedData();

        // Проверяем наличие необходимых данных
        if (!data.contains("RecipeResult", Tag.TAG_LIST) || !data.contains("Ingredients", Tag.TAG_LIST)) {
            return;
        }

        // Проверяем выполнение всех условий рецепта
        if (!validateIngredients(crucible, data)) {
            return;
        }

        ListTag results = data.getList("RecipeResult", Tag.TAG_COMPOUND);
        for (int i = 0; i < results.size(); i++) {
            CompoundTag resultTag = results.getCompound(i);
            ItemStack resultStack = ItemStack.of(resultTag);

            // Основной результат
            ItemEntity itemEntity = new ItemEntity(
                    level,
                    pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                    resultStack.copy()
            );
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);

            // Дополнительный шанс выпадения (если count > 1)
            if (resultStack.getCount() > 1 && level.getRandom().nextFloat() < 0.3f) {
                int extraCount = 1 + level.getRandom().nextInt(3); // от 1 до 3
                ItemStack extraStack = resultStack.copy();
                extraStack.setCount(extraCount);
                ItemEntity extraEntity = new ItemEntity(
                        level,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        extraStack
                );
                extraEntity.setDefaultPickUpDelay();
                level.addFreshEntity(extraEntity);
            }
        }

        // Сбрасываем ингредиенты после успешного выполнения рецепта
        resetIngredients(crucible);
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
    }

    // Метод проверки ингредиентов
    private boolean validateIngredients(ForgeCrucibleEntity crucible, CompoundTag data) {
        ListTag ingredients = data.getList("Ingredients", Tag.TAG_COMPOUND);

        for (int i = 0; i < ingredients.size(); i++) {
            CompoundTag ingTag = ingredients.getCompound(i);
            try {
                JsonObject json = JsonParser.parseString(ingTag.getString("IngredientJson")).getAsJsonObject();
                int required = json.get("count").getAsInt();
                // Создаем ингредиент из JSON
                Ingredient ingredient = Ingredient.fromJson(json);

                // Подсчитываем совпадения без сброса повреждения
                long matches = crucible.depositedItems.stream()
                        .filter(stack -> ingredient.test(stack))
                        .count();

                if (matches < required) {
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    public static void resetIngredients(ForgeCrucibleEntity crucible) {
        crucible.depositedItems.clear();
        crucible.setChanged();
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