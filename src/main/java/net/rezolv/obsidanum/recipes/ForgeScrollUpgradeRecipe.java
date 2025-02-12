package net.rezolv.obsidanum.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.upgrade.ObsidanumToolUpgrades;

import javax.annotation.Nullable;

public class ForgeScrollUpgradeRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients; // Используем ItemStack для хранения ингредиентов с количеством
    private final ItemStack tool;
    private final NonNullList<String> toolTypes; // Новое поле: типы инструментов
    private final NonNullList<String> toolKinds; // Новое поле: виды инструментов
    private final ItemStack output;
    private final ResourceLocation id;
    private final String upgrade;

    public ForgeScrollUpgradeRecipe(NonNullList<Ingredient> ingredients, ItemStack tool,
                                    NonNullList<String> toolTypes, NonNullList<String> toolKinds,
                                    ItemStack output, ResourceLocation id, String upgrade) {
        this.ingredients = ingredients;
        this.tool = tool;
        this.toolTypes = toolTypes;
        this.toolKinds = toolKinds;
        this.output = output;
        this.id = id;
        this.upgrade = upgrade;
    }

    public String getUpgrade() {
        return this.upgrade;
    }

    public NonNullList<String> getToolTypes() {
        return toolTypes;
    }

    public NonNullList<String> getToolKinds() {
        return toolKinds;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        // Проверяем, что инструмент совпадает
        if (!ItemStack.isSameItem(tool, container.getItem(0))) {
            return false;
        }

        // Проверяем, что все ингредиенты совпадают
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient requiredIngredient = ingredients.get(i);
            ItemStack stackInSlot = container.getItem(i + 1); // Слот 0 — инструмент, слоты 1+ — ингредиенты

            if (!requiredIngredient.test(stackInSlot)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }
    public ItemStack getTool() {
        return tool;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.FORGE_SCROLL_UPGRADE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.FORGE_SCROLL_UPGRADE;
    }

    public static class Type implements RecipeType<ForgeScrollUpgradeRecipe> {
        public static final Type FORGE_SCROLL_UPGRADE = new Type();
        public static final String ID = "forge_scroll_upgrade";
    }

    public static class Serializer implements RecipeSerializer<ForgeScrollUpgradeRecipe> {
        public static final Serializer FORGE_SCROLL_UPGRADE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Obsidanum.MOD_ID, "forge_scroll_upgrade");

        @Override
        public ForgeScrollUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            ItemStack tool = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "tool"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "output"));
            String upgrade = GsonHelper.getAsString(serializedRecipe, "upgrade");

            // Чтение tool_types
            NonNullList<String> toolTypes = NonNullList.create();
            if (serializedRecipe.has("tool_types")) {
                JsonArray toolTypesJson = GsonHelper.getAsJsonArray(serializedRecipe, "tool_types");
                for (JsonElement element : toolTypesJson) {
                    toolTypes.add(element.getAsString());
                }
            }

            // Чтение tool_kinds
            NonNullList<String> toolKinds = NonNullList.create();
            if (serializedRecipe.has("tool_kinds")) {
                JsonArray toolKindsJson = GsonHelper.getAsJsonArray(serializedRecipe, "tool_kinds");
                for (JsonElement element : toolKindsJson) {
                    toolKinds.add(element.getAsString());
                }
            }

            // Чтение ингредиентов
            JsonArray ingredientsJson = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for (JsonElement element : ingredientsJson) {
                JsonObject ingredientJson = element.getAsJsonObject();
                // Создаем ItemStack с учетом count
                Item item = GsonHelper.getAsItem(ingredientJson, "item");
                int count = GsonHelper.getAsInt(ingredientJson, "count", 1); // Берём count из JSON
                ItemStack stack = new ItemStack(item, count);
                ingredients.add(Ingredient.of(stack));
            }

            return new ForgeScrollUpgradeRecipe(ingredients, tool, toolTypes, toolKinds, output, recipeId, upgrade);
        }

        @Override
        public @Nullable ForgeScrollUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ItemStack tool = buffer.readItem();

            // Чтение tool_types
            int toolTypesSize = buffer.readVarInt();
            NonNullList<String> toolTypes = NonNullList.withSize(toolTypesSize, "");
            for (int i = 0; i < toolTypesSize; i++) {
                toolTypes.set(i, buffer.readUtf());
            }

            // Чтение tool_kinds
            int toolKindsSize = buffer.readVarInt();
            NonNullList<String> toolKinds = NonNullList.withSize(toolKindsSize, "");
            for (int i = 0; i < toolKindsSize; i++) {
                toolKinds.set(i, buffer.readUtf());
            }

            // Чтение ингредиентов
            int ingredientSize = buffer.readInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientSize, Ingredient.EMPTY);
            for (int i = 0; i < ingredientSize; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            String upgrade = buffer.readUtf();

            return new ForgeScrollUpgradeRecipe(ingredients, tool, toolTypes, toolKinds, output, recipeId, upgrade);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ForgeScrollUpgradeRecipe recipe) {
            buffer.writeItemStack(recipe.tool, false);

            // Запись tool_types
            buffer.writeVarInt(recipe.toolTypes.size());
            for (String type : recipe.toolTypes) {
                buffer.writeUtf(type);
            }

            // Запись tool_kinds
            buffer.writeVarInt(recipe.toolKinds.size());
            for (String kind : recipe.toolKinds) {
                buffer.writeUtf(kind);
            }

            // Запись ингредиентов
            buffer.writeInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItemStack(recipe.output, true);
            buffer.writeUtf(recipe.upgrade);
        }
    }
}