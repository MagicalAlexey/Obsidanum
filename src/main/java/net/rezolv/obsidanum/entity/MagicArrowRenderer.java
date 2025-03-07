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
    }

    @Override
    public ResourceLocation getTextureLocation(MagicArrow entity) {
        return FLAME_ARROW;
    }
}