package net.rezolv.obsidanum.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.projectile_entity.MagicArrow;
import net.rezolv.obsidanum.item.item_entity.arrows.flame_arrow.FlameArrow;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.getOverlayCoords;

public class MagicArrowRenderer extends EntityRenderer<MagicArrow> {
    private final ItemRenderer itemRenderer;
    public static final ResourceLocation FLAME_ARROW = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/projectiles/magic_arrow.png");

    public MagicArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer(); // Инициализация рендера предметов
    }

    @Override
    public void render(MagicArrow entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Получаем вектор скорости
        Vec3 velocity = entity.getDeltaMovement();
        if (velocity.lengthSqr() > 0.0001) {
            Vec3 forward = velocity.normalize();
            float yaw = (float) (Mth.atan2(-forward.x, forward.z) * (180F / Math.PI));
            float pitch = (float) (Mth.atan2(-forward.y, forward.horizontalDistance()) * (180F / Math.PI));

            poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
        }

        // Увеличиваем стрелу
        poseStack.scale(3.0F, 3.0F, 3.0F);

        // Рендерим предмет (стрелу)
        this.itemRenderer.renderStatic(
                entity.getArrowItem(),
                ItemDisplayContext.FIXED, // Положение предмета
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                entity.level(), // ✅ Добавлено, чтобы исправить ошибку
                entity.getId()
        );
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicArrow entity) {
        return FLAME_ARROW;
    }
}