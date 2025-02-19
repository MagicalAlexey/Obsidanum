package net.rezolv.obsidanum.block.forge_crucible.update_ingredients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

import java.util.ListIterator;

public class UpdateIngredientsForgeCrucible {
    public static InteractionResult handleInteraction(Level level, Player player, ItemStack heldStack,
                                                      ForgeCrucibleEntity crucible, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (!heldStack.isEmpty()) {
            return handleItemDeposit(level, player, heldStack, crucible, pos, state);
        } else if (player.isShiftKeyDown()) {
            return handleItemWithdrawal(level, player, crucible, pos, state);
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult handleItemDeposit(Level level, Player player, ItemStack stack,
                                                       ForgeCrucibleEntity crucible, BlockPos pos, BlockState state) {
        Obsidanum.LOGGER.info("Trying to deposit: {}", stack);
        CompoundTag data = crucible.getReceivedData();
        if (!data.contains("Ingredients", Tag.TAG_LIST)) {
            Obsidanum.LOGGER.warn("No ingredients found");
            return InteractionResult.PASS;
        }

        ListTag ingredients = data.getList("Ingredients", Tag.TAG_COMPOUND);
        ItemStack stackCopy = stack.copyWithCount(1);

        for (int i = 0; i < ingredients.size(); i++) {
            CompoundTag ingTag = ingredients.getCompound(i);
            try {
                JsonObject json = JsonParser.parseString(ingTag.getString("IngredientJson")).getAsJsonObject();
                if (processIngredient(crucible, stack, stackCopy, json)) {
                    stack.shrink(1);
                    crucible.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return InteractionResult.SUCCESS;
                }
            } catch (Exception e) {
                Obsidanum.LOGGER.error("Parsing error: {}", e.getMessage());
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean processIngredient(ForgeCrucibleEntity crucible, ItemStack stack,
                                             ItemStack stackCopy, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json);
        if (!ingredient.test(stackCopy)) return false;

        int required = json.get("count").getAsInt();
        long current = crucible.depositedItems.stream()
                .filter(s -> ItemStack.isSameItemSameTags(s, stackCopy))
                .count();

        if (current < required) {
            crucible.depositedItems.add(stackCopy.copy());
            return true;
        }
        return false;
    }

    private static InteractionResult handleItemWithdrawal(Level level, Player player,
                                                          ForgeCrucibleEntity crucible, BlockPos pos, BlockState state) {
        if (crucible.depositedItems.isEmpty()) return InteractionResult.PASS;

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

    private static void giveOrDropItem(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
