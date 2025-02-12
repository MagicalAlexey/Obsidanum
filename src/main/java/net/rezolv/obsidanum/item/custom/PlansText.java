package net.rezolv.obsidanum.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PlansText extends Item {
    public PlansText(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag); // Стандартные подсказки сначала

        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        // Добавляем основной заголовок
        tooltip.add(Component.translatable("tooltip.recipe_information").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));


        // Отображаем результаты
        if (tag.contains("RecipeResult")) {

            ListTag resultList = tag.getList("RecipeResult", Tag.TAG_COMPOUND);
            for (int i = 0; i < resultList.size(); i++) {
                CompoundTag resultTag = resultList.getCompound(i);
                ItemStack result = ItemStack.of(resultTag);
                int count = result.getCount();

                // Название результата
                tooltip.add(
                        result.getHoverName().copy().withStyle(ChatFormatting.WHITE)
                );

                // Количество результата
                tooltip.add(
                        Component.translatable("tooltip.scrolls.quantity") // Ключ локализации
                                .withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(": " + count).withStyle(ChatFormatting.YELLOW))
                );
            }
        }

        // Отображаем ингредиенты
        if (tag.contains("RecipeIngredients")) {
            tooltip.add(Component.translatable("tooltip.scrolls.ingredients").withStyle(ChatFormatting.GOLD));

            ListTag ingredientList = tag.getList("RecipeIngredients", Tag.TAG_COMPOUND);
            for (int i = 0; i < ingredientList.size(); i++) {
                CompoundTag ingredientTag = ingredientList.getCompound(i);
                ItemStack ingredient = ItemStack.of(ingredientTag);
                int count = ingredient.getCount();

                tooltip.add(
                        Component.literal(" - ").withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(count + "x ").withStyle(ChatFormatting.YELLOW))
                                .append(ingredient.getHoverName().copy().withStyle(ChatFormatting.WHITE))
                );
            }
        }

        // Добавляем финальный разделитель
        tooltip.add(Component.translatable("tooltip.recipe_end").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
    }
}
