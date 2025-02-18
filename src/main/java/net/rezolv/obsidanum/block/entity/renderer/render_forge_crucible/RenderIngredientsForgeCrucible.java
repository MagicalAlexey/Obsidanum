package net.rezolv.obsidanum.block.entity.renderer.render_forge_crucible;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.custom.ForgeCrucible;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

import java.util.ArrayList;
import java.util.List;

public class RenderIngredientsForgeCrucible {
    private static final int CYCLE_TICKS = 20; // Смена предмета каждую секунду
    private static final float ITEM_SPACING = 0.3f; // Расстояние между предметами
    private static final float ROTATION_SPEED = 8f; // Скорость вращения (градусов в тик)

    public static void renderIngredients(ForgeCrucibleEntity blockEntity, float partialTick,
                                         PoseStack poseStack, MultiBufferSource buffer,
                                         int packedLight) {

        // Получаем данные из блока
        CompoundTag crucibleData = blockEntity.getReceivedData();
        if (!crucibleData.contains("Ingredients")) return;

        // Читаем список ингредиентов
        ListTag ingredients = crucibleData.getList("Ingredients", CompoundTag.TAG_COMPOUND);
        if (ingredients.isEmpty()) return;

        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Получаем направление блока
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(ForgeCrucible.FACING);

        List<RenderEntry> renderEntries = new ArrayList<>();

        // Подготовка данных для отображения
        for (int i = 0; i < ingredients.size(); i++) {
            CompoundTag entry = ingredients.getCompound(i);
            ListTag itemsList = entry.getList("Items", CompoundTag.TAG_COMPOUND);
            String ingredientJson = entry.getString("IngredientJson");

            // Определяем тип ингредиента
            JsonObject json = JsonParser.parseString(ingredientJson).getAsJsonObject();
            boolean isTag = json.has("tag");

            List<ItemStack> stacks = new ArrayList<>();
            for (int j = 0; j < itemsList.size(); j++) {
                CompoundTag itemTag = itemsList.getCompound(j);
                ItemStack stack = ItemStack.of(itemTag);
                if (!stack.isEmpty()) {
                    stacks.add(stack);
                }
            }

            if (!stacks.isEmpty()) {
                renderEntries.add(new RenderEntry(stacks, isTag));
            }
        }

        // Рассчет позиций
        float totalWidth = (renderEntries.size() - 1) * ITEM_SPACING;
        float startOffset = -totalWidth / 2;
        long gameTime = level.getGameTime();

        // Рендер каждого элемента
        for (int i = 0; i < renderEntries.size(); i++) {
            RenderEntry entry = renderEntries.get(i);
            List<ItemStack> stacks = entry.stacks();

            if (stacks.isEmpty()) continue;

            // Анимация переключения
            int cycleIndex = (int) ((gameTime / CYCLE_TICKS) % stacks.size());
            ItemStack stack = stacks.get(cycleIndex);

            poseStack.pushPose();
            try {
                // Позиционирование в зависимости от направления блока
                float xOffset = 0;
                float zOffset = 0;

                switch (facing) {
                    case NORTH:
                    case SOUTH:
                        xOffset = startOffset + i * ITEM_SPACING;
                        zOffset = 0;
                        break;
                    case EAST:
                    case WEST:
                        xOffset = 0;
                        zOffset = startOffset + i * ITEM_SPACING;
                        break;
                }

                // Фиксированная высота для всех элементов
                poseStack.translate(0.5 + xOffset, 2, 0.5 + zOffset);

                // Вращение вокруг оси Y
                float rotation = (gameTime + partialTick) * ROTATION_SPEED;
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotation));

                // Поворот предмета в соответствии с направлением блока
                switch (facing) {
                    case NORTH:
                        // По умолчанию предмет уже смотрит на север, дополнительный поворот не нужен
                        break;
                    case SOUTH:
                        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180));
                        break;
                    case EAST:
                        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90));
                        break;
                    case WEST:
                        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(270));
                        break;
                    default:
                        break;
                }

                // Масштабирование
                poseStack.scale(0.4f, 0.4f, 0.4f);

                // Рендер предмета
                itemRenderer.renderStatic(
                        stack,
                        ItemDisplayContext.GROUND,
                        getLightLevel(level, pos),
                        OverlayTexture.NO_OVERLAY,
                        poseStack,
                        buffer,
                        level,
                        0
                );
            } finally {
                poseStack.popPose();
            }
        }
    }

    // Вспомогательный класс для хранения данных рендеринга
    private record RenderEntry(List<ItemStack> stacks, boolean isTag) {
        boolean hasMultipleItems() {
            return stacks.size() > 1;
        }
    }

    // Расчет уровня освещения
    private static int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}