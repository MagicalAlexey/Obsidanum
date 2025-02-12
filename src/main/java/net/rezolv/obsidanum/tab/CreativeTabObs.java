package net.rezolv.obsidanum.tab;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.upgrade.ObsidanumToolUpgrades;
import net.rezolv.obsidanum.recipes.*;

public class CreativeTabObs extends CreativeModeTab {

    protected CreativeTabObs(Builder builder) {
        super(builder);
    }
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Obsidanum.MOD_ID);
    public static final RegistryObject<CreativeModeTab> AZURE_OBSIDIAN_BLOCKS_TAB = CREATIVE_MODE_TABS.register("azure_obsidian_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsObs.AZURE_OBSIDIAN_BRICKS.get()))
                    .title(Component.translatable("creativetab.azure_obsidian_blocks"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(BlocksObs.FLAME_DISPENSER.get());
                        pOutput.accept(BlocksObs.FLAME_PIPE.get());
                        pOutput.accept(BlocksObs.CHISELED_AZURE_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.AZURE_OBSIDIAN_COLUMN.get());
                        pOutput.accept(ItemsObs.AZURE_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.AZURE_OBSIDIAN_BRICKS_SLAB.get());
                        pOutput.accept(BlocksObs.AZURE_OBSIDIAN_BRICKS_STAIRS.get());
                        pOutput.accept(BlocksObs.MOLDY_CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.MOLDY_CARVED_OBSIDIAN_BRICKS_E.get());
                        pOutput.accept(BlocksObs.CRACKED_CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_FENCE.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_FENCE.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_FENCE.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_FENCE.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.CRACKED_CARVED_OBSIDIAN_BRICKS_E.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_STAIRS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_SLAB.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_WALL.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_FENCE.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_INLAID_COLUMN.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_INLAID_COLUMN.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_INLAID_COLUMN.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_COLUMN.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_COLUMN.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_WALL.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_WALL.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_WALL.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_COLUMN.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_WALL.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_FENCE.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_WALL.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_WALL.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_FENCE.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_DOOR.get());
                        pOutput.accept(BlocksObs.CARVED_OBSIDIAN_BRICKS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_STAIRS.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_STAIRS.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_LECTERN.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_SLAB.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_SLAB.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_SLAB.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_SLAB.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_FENCE.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_SLAB.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_STAIRS.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_STAIRS.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_STAIRS.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_SLAB.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_STAIRS.get());
                        pOutput.accept(BlocksObs.CARVED_OBSIDIAN_BRICKS_E.get());
                        pOutput.accept(BlocksObs.RIGHT_FORGE_SCROLL.get());
                        pOutput.accept(BlocksObs.FORGE_CRUCIBLE.get());
                        pOutput.accept(BlocksObs.LEFT_CORNER_LEVEL.get());
                        pOutput.accept(BlocksObs.WALL_FORGE_L_CORNER.get());
                        pOutput.accept(BlocksObs.WALL_FORGE_R_CORNER.get());
                        pOutput.accept(BlocksObs.HAMMER_FORGE.get());
                        pOutput.accept(BlocksObs.BAGEL_ANVIL_BLOCK.get());
                        pOutput.accept(BlocksObs.LARGE_ALCHEMICAL_TANK.get());
                        pOutput.accept(BlocksObs.LARGE_ALCHEMICAL_TANK_BROKEN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> OBSIDIAN_BLOCKS_TAB = CREATIVE_MODE_TABS.register("obsidian_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsObs.OBSIDIAN_BRICKS.get()))
                    .title(Component.translatable("creativetab.obsidian_blocks"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ItemsObs.OBSIDAN_SIGN.get());
                        pOutput.accept(ItemsObs.OBSIDAN_HANGING_SIGN.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_FENCE.get());
                        pOutput.accept(BlocksObs.OBSIDAN_FENCE_GATE.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_SLAB.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_DOOR.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_TRAPDOOR.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_BUTTON.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_PRESSURE_PLATE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SAPLING.get());
                        pOutput.accept(BlocksObs.OBSIDAN_PLANKS_STAIRS.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.OBSIDAN_WOOD.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD_LOG.get());
                        pOutput.accept(BlocksObs.STRIPPED_OBSIDAN_WOOD.get());
                        pOutput.accept(ItemsObs.OBSIDAN_WOOD_LEAVES.get());
                        pOutput.accept(ItemsObs.THE_GLOOMY_MYCELIUM.get());
                        pOutput.accept(BlocksObs.ONYX.get());
                        pOutput.accept(BlocksObs.ONYX_SLAB.get());
                        pOutput.accept(BlocksObs.ONYX_STAIRS.get());
                        pOutput.accept(BlocksObs.ONYX_BRICKS.get());
                        pOutput.accept(BlocksObs.ONYX_BRICKS_SLAB.get());
                        pOutput.accept(BlocksObs.ONYX_BRICKS_STAIRS.get());
                        pOutput.accept(BlocksObs.POLISHED_ONYX.get());
                        pOutput.accept(BlocksObs.POLISHED_ONYX_SLAB.get());
                        pOutput.accept(BlocksObs.POLISHED_ONYX_STAIRS.get());
                        pOutput.accept(BlocksObs.LOCKED_CHEST_RUNIC.get());

                        pOutput.accept(BlocksObs.OBSIDIAN_TABLET.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_CHEST.get());
                        pOutput.accept(BlocksObs.RUNIC_OBSIDIAN_CHEST.get());


                        pOutput.accept(BlocksObs.MOLDY_CARVED_OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.CRACKED_CARVED_OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_FENCE_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_FENCE_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_FENCE_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_FENCE_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_STAIRS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_SLAB_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_WALL_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_FENCE_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_INLAID_COLUMN_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_INLAID_COLUMN_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_INLAID_COLUMN_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_COLUMN_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_COLUMN_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_COLUMN_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_WALL_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_WALL_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_WALL_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_WALL_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_WALL_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_WALL_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_FENCE_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_FENCE_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_FENCE_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_D.get());
                        pOutput.accept(BlocksObs.CARVED_OBSIDIAN_BRICKS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_STAIRS_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_STAIRS_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_STAIRS_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_STAIRS_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_STAIRS_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_STAIRS_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_BRICKS_SLAB_D.get());
                        pOutput.accept(BlocksObs.CRACKED_OBSIDIAN_POLISHED_SLAB_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_POLISHED_SLAB_D.get());
                        pOutput.accept(BlocksObs.MOLDY_OBSIDIAN_BRICKS_SLAB_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_POLISHED_SLAB_D.get());
                        pOutput.accept(BlocksObs.OBSIDIAN_BRICKS_SLAB_D.get());
                        pOutput.accept(BlocksObs.STEM_GLOOMY_MUSHROOM.get());
                        pOutput.accept(BlocksObs.HEAD_HYMENIUM_STEM_GLOOMY_MUSHROOM.get());
                        pOutput.accept(ItemsObs.CAP_GLOOMY_MUSHROOM.get());
                        pOutput.accept(BlocksObs.VELNARIUM_GRID.get());
                        pOutput.accept(BlocksObs.FLAME_BANNER_BAGGEL.get());
                        pOutput.accept(BlocksObs.DECORATIVE_URN.get());
                        pOutput.accept(BlocksObs.UNUSUAL_DECORATIVE_URN.get());
                        pOutput.accept(BlocksObs.RARE_DECORATIVE_URN.get());
                        pOutput.accept(BlocksObs.LARGE_URN.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> OBSIDANUM_TAB = CREATIVE_MODE_TABS.register("obsidanum_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsObs.OBSIDAN.get()))
                    .title(Component.translatable("creativetab.obsidanum"))
                    .displayItems((pParameters, pOutput) -> {
                        //Items

                        pOutput.accept(ItemsObs.OBSIDIAN_TEAR.get());
                        pOutput.accept(ItemsObs.FLAME_CROSSBOW.get());
                        pOutput.accept(ItemsObs.FLAME_BOLT.get());
                        pOutput.accept(ItemsObs.OBSIDAN.get());
                        pOutput.accept(ItemsObs.RAW_MEET_BEETLE.get());
                        pOutput.accept(ItemsObs.COCKED_MEET_BEETLE.get());
                        pOutput.accept(ItemsObs.DRILLING_CRYSTALLIZER.get());
                        pOutput.accept(ItemsObs.OBSIDAN_AXE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.OBSIDAN_SWORD.get());
                        pOutput.accept(ItemsObs.REZOLV_THE_TALE_OF_THE_VANISHED_ORDER_DISC.get());
                        pOutput.accept(ItemsObs.OBSIDAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_APPLE.get());
                        pOutput.accept(ItemsObs.OBSIDAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_ARROW.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_AXE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_PICKAXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_AXE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_HOE.get());
                        pOutput.accept(ItemsObs.SMOLDERING_OBSIDIAN_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHOVEL.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_HOE.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_CHAKRAM.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_IRON_ORE.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_GOLD_ORE.get());
                        pOutput.accept(ItemsObs.CRYSTALLIZED_COPPER_ORE.get());
                        pOutput.accept(ItemsObs.BAGELL_FUEL.get());

                        pOutput.accept(ItemsObs.OBSIDAN_BOAT.get());
                        pOutput.accept(ItemsObs.OBSIDAN_CHEST_BOAT.get());
                        pOutput.accept(ItemsObs.NETHER_FLAME.get());
                        pOutput.accept(ItemsObs.CRUCIBLE.get());
                        pOutput.accept(ItemsObs.VELNARIUM_INGOT.get());
                        pOutput.accept(ItemsObs.GLOOMY_MUSHROOM.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHARD_KEY.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHARD_ARROW.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_SHARD_INVIOLABILITY.get());
                        pOutput.accept(ItemsObs.FACETED_ONYX.get());
                        pOutput.accept(ItemsObs.ONYX_PENDANT.get());
                        pOutput.accept(ItemsObs.EYE_GART.get());
                        pOutput.accept(ItemsObs.RELICT_AMETHYST_SHARD.get());
                        pOutput.accept(ItemsObs.CRUCIBLE_WITH_NETHER_FLAME.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_DOOR_KEY_1.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_DOOR_KEY_2.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_DOOR_KEY_3.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_DOOR_KEY_4.get());
                        pOutput.accept(ItemsObs.VELNARIUM_ORE.get());
                        pOutput.accept(ItemsObs.ORDER_SWORD.get());
                        pOutput.accept(ItemsObs.OBSIDIAN_TOTEM_OF_IMMORTALITY.get());
                        pOutput.accept(ItemsObs.ELEMENTAL_CRUSHER.get());
                        pOutput.accept(ItemsObs.VELNARIUM_MACE.get());
                        pOutput.accept(ItemsObs.DEMONIC_BONECRUSHER.get());
                        pOutput.accept(ItemsObs.SPORES_OF_THE_GLOOMY_MUSHROOM.get());
                        pOutput.accept(ItemsObs.OBSIDAN_ESSENCE.get());
                        pOutput.accept(ItemsObs.FIERY_INFUSION_SMITHING_TEMPLATE.get());
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> SCROLLS_TAB = CREATIVE_MODE_TABS.register("scrolls_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemsObs.ORDER_PLAN.get()))
                    .title(Component.translatable("creativetab.scrolls"))
                    .displayItems((pParameters, pOutput) -> {

                        Level level = Minecraft.getInstance().getCameraEntity().level();
                        if (level == null) {
                            return; // 🐗
                        }

                        level.getRecipeManager().getRecipes().forEach(recipe -> {
                            ItemStack result = null;

                            if (recipe.getType() == ForgeScrollNetherRecipe.Type.FORGE_SCROOL_NETHER) {
                                result = ItemsObs.NETHER_PLAN.get().getDefaultInstance();
                            } else if (recipe.getType() == ForgeScrollOrderRecipe.Type.FORGE_SCROOL_ORDER) {
                                result = ItemsObs.ORDER_PLAN.get().getDefaultInstance();
                            } else if (recipe.getType() == ForgeScrollCatacombsRecipe.Type.FORGE_SCROOL_CATACOMBS) {
                                result = ItemsObs.CATACOMBS_PLAN.get().getDefaultInstance();
                            } else if (recipe.getType() == ForgeScrollUpgradeRecipe.Type.FORGE_SCROLL_UPGRADE) {
                                result = ItemsObs.UPGRADE_PLAN.get().getDefaultInstance();
                            }

                            if (result != null && !result.isEmpty()) {
                                CompoundTag resultTag = result.getOrCreateTag();
                                resultTag.putString("RecipesPlans", "recipes/" + recipe.getId().getPath());

                                // Handle different recipe types
                                if (recipe instanceof ForgeScrollNetherRecipe forgeScrollNetherRecipe) {
                                    handleScrollNetherRecipe(result, resultTag, level, forgeScrollNetherRecipe);
                                } else if (recipe instanceof ForgeScrollOrderRecipe forgeScrollOrderRecipe) {
                                    handleScrollOrderRecipe(result, resultTag, level, forgeScrollOrderRecipe);
                                } else if (recipe instanceof ForgeScrollCatacombsRecipe forgeScrollCatacombsRecipe) {
                                    handleScrollCatacombsRecipe(result, resultTag, level, forgeScrollCatacombsRecipe);
                                } else if (recipe instanceof ForgeScrollUpgradeRecipe forgeScrollUpgradeRecipe) {
                                    handleScrollUpgradeRecipe(result, resultTag, level, forgeScrollUpgradeRecipe);
                                }
                                result.setCount(1);

                                pOutput.accept(result);
                            }
                        });
                    })
                    .build());

    private static void handleScrollUpgradeRecipe(ItemStack result, CompoundTag resultTag, Level level, ForgeScrollUpgradeRecipe recipe) {
        resultTag.putString("RecipeId", recipe.getId().toString());

        // Сохраняем инструмент
        CompoundTag toolTag = new CompoundTag();
        recipe.getTool().save(toolTag);
        resultTag.put("Tool", toolTag);

        // Сохраняем результат
        CompoundTag outputTag = new CompoundTag();
        recipe.getResultItem(level.registryAccess()).save(outputTag);
        resultTag.put("Output", outputTag);

        // Сохраняем улучшение
        resultTag.putString("Upgrade", recipe.getUpgrade());

        // Сохраняем ингредиенты (с поддержкой тегов и альтернатив)
        ListTag ingredientsTag = new ListTag();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length > 0) {
                JsonObject json = new JsonObject();
                json.addProperty("item", ForgeRegistries.ITEMS.getKey(stacks[0].getItem()).toString());
                json.addProperty("count", stacks[0].getCount()); // Сохраняем count
                ingredientsTag.add(StringTag.valueOf(json.toString()));
            }
        }
        resultTag.put("Ingredients", ingredientsTag);

        // Сохраняем tool_types и tool_kinds
        ListTag toolTypesTag = new ListTag();
        for (String type : recipe.getToolTypes()) {
            toolTypesTag.add(StringTag.valueOf(type));
        }
        resultTag.put("ToolTypes", toolTypesTag);

        ListTag toolKindsTag = new ListTag();
        for (String kind : recipe.getToolKinds()) {
            toolKindsTag.add(StringTag.valueOf(kind));
        }
        resultTag.put("ToolKinds", toolKindsTag);

        result.setTag(resultTag);
    }
    private static void handleScrollNetherRecipe(ItemStack result, CompoundTag resultTag, Level level, ForgeScrollNetherRecipe forgeScrollNetherRecipe) {
        // Получаем количество результата из JSON
        ItemStack outputStack = forgeScrollNetherRecipe.getResultItem(level.registryAccess());
        int resultCount = outputStack.getCount();

        // Устанавливаем количество для ItemStack результата
        result.setCount(resultCount);

        // Проверка результата
        System.out.println("Количество из рецепта: " + resultCount);

        // Добавляем ингредиенты рецепта
        ListTag ingredientList = new ListTag();
        for (ItemStack ingredient : forgeScrollNetherRecipe.getInputItems()) {
            CompoundTag ingredientTag = new CompoundTag();
            ingredient.save(ingredientTag);
            ingredientTag.putInt("Count", ingredient.getCount());
            ingredientList.add(ingredientTag);
        }
        resultTag.put("RecipeIngredients", ingredientList);

        // Добавляем результат рецепта
        ListTag resultList = new ListTag();
        CompoundTag outputTag = new CompoundTag();
        outputStack.save(outputTag);
        outputTag.putInt("Count", resultCount); // Устанавливаем количество в теге
        resultList.add(outputTag);
        resultTag.put("RecipeResult", resultList);

        // Дополнительный отладочный вывод для проверки всех данных
        System.out.println("ItemStack результата: " + result);
        System.out.println("Тег результата: " + resultTag);
    }

    private static void handleScrollOrderRecipe(ItemStack result, CompoundTag resultTag, Level level, ForgeScrollOrderRecipe forgeScrollOrderRecipe) {
        result.setCount(forgeScrollOrderRecipe.getResultItem(level.registryAccess()).getCount());

        // Добавляем ингредиенты рецепта
        ListTag ingredientList = new ListTag();
        for (ItemStack ingredient : forgeScrollOrderRecipe.getInputItems()) {
            CompoundTag ingredientTag = new CompoundTag();
            ingredient.save(ingredientTag);
            ingredientTag.putInt("Count", ingredient.getCount());
            ingredientList.add(ingredientTag);
        }
        resultTag.put("RecipeIngredients", ingredientList);

        // Добавляем результат рецепта
        ListTag resultList = new ListTag();
        CompoundTag outputTag = new CompoundTag();
        forgeScrollOrderRecipe.getResultItem(level.registryAccess()).save(outputTag);
        outputTag.putInt("Count", result.getCount()); // Обновляем количество
        resultList.add(outputTag);
        resultTag.put("RecipeResult", resultList);

        // Отладочный вывод для проверки данных
        System.out.println("Созданный предмет результата (Order): " + result);
        System.out.println("Тег результата (Order): " + resultTag);
    }

    private static void handleScrollCatacombsRecipe(ItemStack result, CompoundTag resultTag, Level level, ForgeScrollCatacombsRecipe forgeScrollCatacombsRecipe) {
        result.setCount(forgeScrollCatacombsRecipe.getResultItem(level.registryAccess()).getCount());

        // Добавляем ингредиенты рецепта
        ListTag ingredientList = new ListTag();
        for (ItemStack ingredient : forgeScrollCatacombsRecipe.getInputItems()) {
            CompoundTag ingredientTag = new CompoundTag();
            ingredient.save(ingredientTag);
            ingredientTag.putInt("Count", ingredient.getCount());
            ingredientList.add(ingredientTag);
        }
        resultTag.put("RecipeIngredients", ingredientList);

        // Добавляем результат рецепта
        ListTag resultList = new ListTag();
        CompoundTag outputTag = new CompoundTag();
        forgeScrollCatacombsRecipe.getResultItem(level.registryAccess()).save(outputTag);
        outputTag.putInt("Count", result.getCount()); // Обновляем количество
        resultList.add(outputTag);
        resultTag.put("RecipeResult", resultList);

        // Отладочный вывод для проверки данных
        System.out.println("Созданный предмет результата (Catacombs): " + result);
        System.out.println("Тег результата (Catacombs): " + resultTag);
    }



}