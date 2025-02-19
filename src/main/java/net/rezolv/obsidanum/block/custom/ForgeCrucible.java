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
        if (!(be instanceof ForgeCrucibleEntity crucibleEntity)) {
            return InteractionResult.PASS;
        }

        // Получаем копию NBT данных блока
        CompoundTag data = crucibleEntity.getReceivedData();
        if (!data.contains("Ingredients", Tag.TAG_LIST)) {
            System.out.println("Ключ Ingredients отсутствует в NBT");
            return InteractionResult.PASS;
        }

        // В NBT Ingredients – список Compound, где каждый имеет поле "IngredientJson"
        ListTag ingredientsList = data.getList("Ingredients", Tag.TAG_COMPOUND);
        boolean modified = false;

        // Вывод для отладки: печатаем содержимое каждого ингредиента
        for (int i = 0; i < ingredientsList.size(); i++) {
            CompoundTag ingredientCompound = ingredientsList.getCompound(i);
            String jsonStr = ingredientCompound.getString("IngredientJson");
            System.out.println("Ingredient[" + i + "]: " + jsonStr);
        }

        ItemStack heldStack = player.getItemInHand(hand);

        // Если игрок держит предмет – кладём его (удаляем 1 единицу из руки, уменьшаем count)
        if (!heldStack.isEmpty()) {
            for (int i = 0; i < ingredientsList.size(); i++) {
                CompoundTag ingredientCompound = ingredientsList.getCompound(i);
                String jsonStr = ingredientCompound.getString("IngredientJson");
                JsonObject json;
                try {
                    json = JsonParser.parseString(jsonStr).getAsJsonObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                // Извлекаем ID и count
                String ingrItemId = json.has("item") ? json.get("item").getAsString() : "";
                int count = json.has("count") ? json.get("count").getAsInt() : 0;
                ResourceLocation rl = new ResourceLocation(ingrItemId);
                Item ingredientItem = ForgeRegistries.ITEMS.getValue(rl);
                // Если предмет в руке соответствует ингредиенту и count > 0
                if (ingredientItem != null && heldStack.getItem() == ingredientItem) {
                    if (count > 0) {
                        heldStack.shrink(1);
                        json.addProperty("count", count - 1);
                        ingredientCompound.putString("IngredientJson", json.toString());
                        ingredientsList.set(i, ingredientCompound);
                        modified = true;
                        System.out.println("Депозит: уменьшили count для " + ingrItemId + " с " + count + " до " + (count - 1));
                        break;
                    } else {
                        System.out.println("Для " + ingrItemId + " count равен 0");
                    }
                }
            }
        }
        // Если рука пуста и нажата Shift – возвращаем предмет (увеличиваем count)
        else if (player.isShiftKeyDown()) {
            for (int i = 0; i < ingredientsList.size(); i++) {
                CompoundTag ingredientCompound = ingredientsList.getCompound(i);
                String jsonStr = ingredientCompound.getString("IngredientJson");
                JsonObject json;
                try {
                    json = JsonParser.parseString(jsonStr).getAsJsonObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                String ingrItemId = json.has("item") ? json.get("item").getAsString() : "";
                int count = json.has("count") ? json.get("count").getAsInt() : 0;
                ResourceLocation rl = new ResourceLocation(ingrItemId);
                Item ingredientItem = ForgeRegistries.ITEMS.getValue(rl);
                if (ingredientItem != null) {
                    json.addProperty("count", count + 1);
                    ingredientCompound.putString("IngredientJson", json.toString());
                    ingredientsList.set(i, ingredientCompound);
                    modified = true;
                    ItemStack returnStack = new ItemStack(ingredientItem, 1);
                    if (!player.getInventory().add(returnStack)) {
                        player.drop(returnStack, false);
                    }
                    System.out.println("Выдача: увеличили count для " + ingrItemId + " с " + count + " до " + (count + 1));
                    break;
                }
            }
        }

        if (modified) {
            data.put("Ingredients", ingredientsList);
            crucibleEntity.receiveScrollData(data);
            level.sendBlockUpdated(pos, state, state, 3);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
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