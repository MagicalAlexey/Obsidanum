package net.rezolv.obsidanum.block.entity.renderer.render_forge_crucible;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

public class RenderResultForgeCrucible {

    public static void renderResult(ForgeCrucibleEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                                    MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        CompoundTag crucibleData = pBlockEntity.getReceivedData();

        if (crucibleData.contains("RecipeResult")) {
            ListTag recipeResult = crucibleData.getList("RecipeResult", 10);

            if (!recipeResult.isEmpty()) {
                CompoundTag resultTag = recipeResult.getCompound(0);
                ItemStack resultStack = ItemStack.of(resultTag);

                if (!resultStack.isEmpty()) {
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    Level level = pBlockEntity.getLevel();
                    BlockPos pos = pBlockEntity.getBlockPos();
                    int count = resultStack.getCount();

                    // Анимация плавающего движения
                    float time = (level.getGameTime() + pPartialTick) * 0.05f;
                    float yOffset = (float) Math.sin(time) * 0.04f; // Амплитуда 0.15 блока

                    pPoseStack.pushPose();
                    pPoseStack.translate(0.5, 1.5 + yOffset, 0.5); // Стартовая высота 1.35 + анимация
                    pPoseStack.scale(0.5f, 0.5f, 0.5f);

                    // Рендер предмета без вращения
                    itemRenderer.renderStatic(
                            resultStack,
                            ItemDisplayContext.GROUND,
                            getLightLevel(level, pos),
                            OverlayTexture.NO_OVERLAY,
                            pPoseStack,
                            pBuffer,
                            level,
                            0
                    );
                    pPoseStack.popPose();

                }
            }
        }
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}