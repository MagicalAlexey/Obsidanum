package net.rezolv.obsidanum.block.forge_crucible.update_ingredients;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

public class UpdateIngredientsForgeCrucible {
    public static InteractionResult handleUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            ItemStack heldItem = player.getItemInHand(hand);

            if (heldItem.getItem() == Items.STICK) {
                BlockEntity blockEntity = level.getBlockEntity(pos);

                if (blockEntity instanceof ForgeCrucibleEntity crucible) {
                    CompoundTag crucibleData = crucible.getReceivedData();

                    System.out.println("[Crucible] Player used stick. Stored NBT:");
                    System.out.println("Position: " + pos);
                    System.out.println("Player: " + player.getName().getString());
                    System.out.println("NBT Data: " + crucibleData);

                    if (crucibleData.contains("Ingredients")) {
                        System.out.println("Ingredients: " + crucibleData.get("Ingredients"));
                    }
                    if (crucibleData.contains("RecipeResult")) {
                        System.out.println("RecipeResult: " + crucibleData.get("RecipeResult"));
                    }
                    if (crucibleData.contains("RecipesPlans")) {
                        System.out.println("RecipesPlans: " + crucibleData.getString("RecipesPlans"));
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
