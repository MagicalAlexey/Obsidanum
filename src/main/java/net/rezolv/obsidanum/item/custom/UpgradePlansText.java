package net.rezolv.obsidanum.item.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
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
import net.minecraftforge.registries.ForgeRegistries;

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
        if (tag == null) return;

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
            ListTag ingredientsTag = tag.getList("Ingredients", Tag.TAG_STRING);

            if (Screen.hasShiftDown()) {
                addFullIngredientsList(tooltip, ingredientsTag);
            } else {
                addCollapsedIngredientsPreview(tooltip, ingredientsTag);
            }
        }

        // Добавляем разделитель в конце
        tooltip.add(Component.translatable("tooltip.recipe_end").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
    }
    private void addCollapsedIngredientsPreview(List<Component> tooltip, ListTag ingredientsTag) {
        if (ingredientsTag.isEmpty()) return;

        tooltip.add(Component.translatable("tooltip.scrolls.ingredients").withStyle(ChatFormatting.GOLD));

        int hiddenTags = 0; // Счётчик свёрнутых тегов

        for (int i = 0; i < ingredientsTag.size(); i++) {
            String jsonStr = ingredientsTag.getString(i);
            JsonObject ingredientJson = JsonParser.parseString(jsonStr).getAsJsonObject();

            int count = 1;
            if (ingredientJson.has("count")) {
                JsonElement countElement = ingredientJson.get("count");
                if (countElement.isJsonPrimitive() && countElement.getAsJsonPrimitive().isNumber()) {
                    count = countElement.getAsInt();
                }
            }

            MutableComponent line = Component.literal(" - ").withStyle(ChatFormatting.GRAY);

            if (ingredientJson.has("tag")) {
                ResourceLocation tagId = new ResourceLocation(ingredientJson.get("tag").getAsString());
                line.append(getTagComponent(tagId, count, false));
                hiddenTags++; // Увеличиваем счётчик тегов
            } else if (ingredientJson.has("item")) {
                ResourceLocation itemId = new ResourceLocation(ingredientJson.get("item").getAsString());
                line.append(getItemComponent(itemId, count)); // Предметы показываются полностью
            }

            tooltip.add(line);
        }

        // Подсказка о Shift только если есть свёрнутые теги
        if (hiddenTags > 0) {
            tooltip.add(Component.translatable("obsidanum.press_shift_for_tags")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private void addFullIngredientsList(List<Component> tooltip, ListTag ingredientsTag) {
        tooltip.add(Component.translatable("tooltip.scrolls.ingredients")
                .withStyle(ChatFormatting.GOLD));

        for (int i = 0; i < ingredientsTag.size(); i++) {
            String jsonStr = ingredientsTag.getString(i);
            JsonObject ingredientJson = JsonParser.parseString(jsonStr).getAsJsonObject();

            int count = 1;
            if (ingredientJson.has("count")) {
                JsonElement countElement = ingredientJson.get("count");
                if (countElement.isJsonPrimitive() && countElement.getAsJsonPrimitive().isNumber()) {
                    count = countElement.getAsInt();
                }
            }

            MutableComponent line = Component.literal(" - ").withStyle(ChatFormatting.GRAY);

            if (ingredientJson.has("tag")) {
                ResourceLocation tagId = new ResourceLocation(ingredientJson.get("tag").getAsString());
                line.append(getTagComponent(tagId, count, true)); // Полное отображение тега
            } else if (ingredientJson.has("item")) {
                ResourceLocation itemId = new ResourceLocation(ingredientJson.get("item").getAsString());
                line.append(getItemComponent(itemId, count));
            }

            tooltip.add(line);
        }
    }
    private Component getTagComponent(ResourceLocation tagId, int count, boolean expanded) {
        TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagId);
        List<Item> items = ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList();

        if (items.isEmpty()) {
            return Component.literal(count + "x #" + tagId).withStyle(ChatFormatting.RED);
        }

        MutableComponent component = Component.literal(count + "x ");

        if (expanded) {
            // Режим с Shift: показываем все элементы тега
            component.append(Component.literal("[Тег] ").withStyle(ChatFormatting.DARK_GRAY));
            for (Item item : items) {
                component.append("\n   ").append(new ItemStack(item).getHoverName());
            }
        } else {
            // Свёрнутый режим: первый элемент + количество
            Item firstItem = items.get(0);
            component.append(firstItem.getDescription())
                    .append(Component.literal(" +" + (items.size() - 1))
                            .withStyle(ChatFormatting.DARK_GRAY));

            // Подсказка при наведении
            MutableComponent hoverContent = Component.literal("Содержимое тега:")
                    .withStyle(ChatFormatting.GRAY);
            for (Item item : items) {
                hoverContent.append("\n• ").append(item.getDescription());
            }

            component.withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverContent)));
        }

        return component;
    }

    private Component getItemComponent(ResourceLocation itemId, int count) {
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item != null) {
            return Component.literal(count + "x ").append(new ItemStack(item).getHoverName());
        }
        return Component.literal(count + "x " + itemId.toString())
                .withStyle(ChatFormatting.RED);
    }
}