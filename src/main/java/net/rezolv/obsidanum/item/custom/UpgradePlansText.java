package net.rezolv.obsidanum.item.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpgradePlansText extends Item {
    public UpgradePlansText(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // Получаем тег предмета
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return; // Если тега нет, ничего не делаем
        }

        // Добавляем заголовок
        tooltip.add(Component.translatable("tooltip.recipe_information").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));

        // Отображаем тип улучшения
        if (tag.contains("Upgrade")) {
            String upgrade = tag.getString("Upgrade").toUpperCase();
            tooltip.add(Component.literal(upgrade).withStyle(ChatFormatting.GREEN));
        }

        // Отображаем tool_types с иконками
        if (tag.contains("ToolTypes")) {
            MutableComponent icons = Component.literal(" ");
            ListTag toolTypesTag = tag.getList("ToolTypes", Tag.TAG_STRING);

            for (int i = 0; i < toolTypesTag.size(); i++) {
                String toolType = toolTypesTag.getString(i);
                switch (toolType.toLowerCase()) {
                    case "pickaxe" -> icons.append(Component.literal("\uE000").withStyle(Style.EMPTY.withFont(new ResourceLocation("obsidanum", "tool_icons"))));
                    case "axe" -> icons.append(Component.literal("\uE001").withStyle(Style.EMPTY.withFont(new ResourceLocation("obsidanum", "tool_icons"))));
                    case "shovel" -> icons.append(Component.literal("\uE002").withStyle(Style.EMPTY.withFont(new ResourceLocation("obsidanum", "tool_icons"))));
                    case "sword" -> icons.append(Component.literal("\uE003").withStyle(Style.EMPTY.withFont(new ResourceLocation("obsidanum", "tool_icons"))));
                    case "hoe" -> icons.append(Component.literal("\uE004").withStyle(Style.EMPTY.withFont(new ResourceLocation("obsidanum", "tool_icons"))));
                }
            }

            tooltip.add(icons);
        }

        // Отображаем tool_kinds
        if (tag.contains("ToolKinds")) {
            tooltip.add(Component.translatable("tooltip.scrolls.tool_kinds").withStyle(ChatFormatting.GOLD));

            ListTag toolKindsTag = tag.getList("ToolKinds", Tag.TAG_STRING);
            for (int i = 0; i < toolKindsTag.size(); i++) {
                String toolKind = toolKindsTag.getString(i);
                tooltip.add(Component.literal(" - ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(toolKind).withStyle(ChatFormatting.WHITE)));
            }
        }

        // Отображаем ингредиенты
        if (tag.contains("Ingredients")) {
            tooltip.add(Component.translatable("tooltip.scrolls.ingredients").withStyle(ChatFormatting.GOLD));

            ListTag ingredientsTag = tag.getList("Ingredients", Tag.TAG_STRING);
            for (int i = 0; i < ingredientsTag.size(); i++) {
                String jsonStr = ingredientsTag.getString(i);
                JsonElement json = JsonParser.parseString(jsonStr);
                JsonObject jsonObject = json.getAsJsonObject();

                // Извлекаем количество из JSON
                int count = jsonObject.has("count") ? jsonObject.get("count").getAsInt() : 1;

                Ingredient ingredient = Ingredient.fromJson(json);
                MutableComponent line = Component.literal(" - ")
                        .append(getIngredientText(ingredient, count)); // Передаем count// Передаём count

                tooltip.add(line);
            }
        }

        // Добавляем разделитель в конце
        tooltip.add(Component.translatable("tooltip.recipe_end").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
    }
    private Component getIngredientText(Ingredient ingredient, int count) { // Добавлен параметр count
        ItemStack[] items = ingredient.getItems();
        if (items.length > 0) {
            if (items.length == 1) {
                return Component.literal(count + "x ") // Используем переданный count
                        .withStyle(ChatFormatting.YELLOW)
                        .append(items[0].getHoverName());
            } else {
                MutableComponent firstItemName = Component.literal(count + "x ") // Используем переданный count
                        .withStyle(ChatFormatting.WHITE)
                        .append(items[0].getHoverName());
                return firstItemName.withStyle(style ->
                        style.withHoverEvent(
                                new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Component.literal("Доступные предметы:")
                                                .append("\n" + Arrays.stream(items)
                                                        .map(stack -> stack.getCount() + "x " + stack.getHoverName().getString())
                                                        .collect(Collectors.joining("\n")))
                                )
                        ));
            }
        } else {
            return Component.literal("???");
        }
    }
}