package net.rezolv.obsidanum.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

import java.util.List;

public class ForgeCrucibleEntityRenderer implements BlockEntityRenderer<ForgeCrucibleEntity> {
    public ForgeCrucibleEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ForgeCrucibleEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        Level level = pBlockEntity.getLevel();
        BlockPos pos = pBlockEntity.getBlockPos();

        // Проверяем наличие лавы ниже через один блок
        if (level.getBlockState(pos.below(2)).is(BlocksObs.NETHER_FLAME_BLOCK.get())) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

            // Получаем список ингредиентов
            List<ItemStack> ingredients = pBlockEntity.getIngredients();

            long time = level.getGameTime();
            double animationOffset = Math.sin((time + pPartialTick) * 0.1) * 0.025;
            pPoseStack.translate(0.5f, 1.75f + animationOffset, 0.5f);
            pPoseStack.scale(0.30f, 0.30f, 0.30f);

            pPoseStack.pushPose();

            Direction facing = pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            switch (facing) {
                case NORTH:
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
                    break;
                case EAST:
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                    break;
                case SOUTH:
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
                    break;
                case WEST:
                    pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
                    break;
                default:
                    break;
            }

            // Рендеринг результата (внизу)
            ItemStack output = pBlockEntity.getOutput();
            if (!output.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.0f, -0.6f, 0.0f); // Смещение результата вниз
                pPoseStack.scale(1.5f, 1.5f, 1.5f); // Увеличение масштаба результата
                itemRenderer.renderStatic(output, ItemDisplayContext.FIXED,
                        getLightLevel(level, pos),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, level, 1);
                pPoseStack.popPose();
            }

            // Рендеринг ингредиентов (в одну линию, выше блока)
            float itemSpacing = 1.2f;  // Расстояние между предметами
            float startX = -(ingredients.size() - 1) * itemSpacing / 2; // Центровка ингредиентов по горизонтали

            for (int i = 0; i < ingredients.size(); i++) {
                ItemStack itemStack = ingredients.get(i);

                pPoseStack.pushPose();

                // Расчёт позиции каждого ингредиента
                float offsetX = startX + i * itemSpacing;
                pPoseStack.translate(offsetX, 1.2f, 0.0f); // Смещение ингредиентов выше блока
                itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                        getLightLevel(level, pos),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, level, 1);
                pPoseStack.popPose();
            }

            pPoseStack.popPose();
        }
    }
    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}