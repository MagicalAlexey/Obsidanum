package net.rezolv.obsidanum.effect.effects.effect_overlay;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.rezolv.obsidanum.effect.EffectsObs;

public class ConfusionOverlay {

    private static final ResourceLocation[] OVERLAY_TEXTURES = {
            new ResourceLocation("obsidanum", "textures/overlay/flash1.png"),
            new ResourceLocation("obsidanum", "textures/overlay/flash2.png"),
            new ResourceLocation("obsidanum", "textures/overlay/flash3.png"),
            new ResourceLocation("obsidanum", "textures/overlay/flash4.png"),
            new ResourceLocation("obsidanum", "textures/overlay/flash5.png")
    };

    private static final int TICK_INTERVAL = 1; // Смена картинки каждые 5 тиков
    private int currentIndex = 0; // Текущая текстура
    private int tickCounter = 0; // Счетчик тиков

    public ConfusionOverlay() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.player.hasEffect(EffectsObs.FLASH.get())) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // Включение текстур и настроек прозрачности
        RenderSystem.enableBlend();

        RenderSystem.defaultBlendFunc(); // Стандартное смешивание
        RenderSystem.setShaderTexture(1, OVERLAY_TEXTURES[currentIndex]);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.7f);
        // Отрисовка с прозрачностью
        guiGraphics.blit(OVERLAY_TEXTURES[currentIndex], 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

        // Отключение смешивания для других элементов интерфейса
        RenderSystem.disableBlend();
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        tickCounter++;
        if (tickCounter >= TICK_INTERVAL) {
            tickCounter = 0;
            currentIndex = (currentIndex + 1) % OVERLAY_TEXTURES.length;
        }
    }
}
