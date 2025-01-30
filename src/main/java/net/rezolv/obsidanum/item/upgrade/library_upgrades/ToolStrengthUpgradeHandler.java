package net.rezolv.obsidanum.item.upgrade.library_upgrades;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.upgrade.ObsidanumToolUpgrades;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class ToolStrengthUpgradeHandler {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;

        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

        // Проверяем, есть ли палка в одной руке, а инструмент или меч в другой
        if ((mainHandItem.is(Items.STICK) && isUpgradeable(offHandItem)) || (offHandItem.is(Items.STICK) && isUpgradeable(mainHandItem))) {
            ItemStack tool = mainHandItem.is(Items.STICK) ? offHandItem : mainHandItem;

            // Добавляем тег улучшения
            CompoundTag tag = tool.getOrCreateTag();
            tag.putString("strength_upgrade", ObsidanumToolUpgrades.STRENGTH.getName());
            tool.setTag(tag);


            ListTag lore = new ListTag();
            lore.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("hello").withStyle(ChatFormatting.DARK_AQUA))));
            CompoundTag display = tool.getOrCreateTagElement("display");
            display.put("Lore", lore);
        }
    }

    private static boolean isUpgradeable(@NotNull ItemStack stack) {
        return stack.isDamageableItem() || stack.isEnchanted() || stack.getItem().canPerformAction(stack, ToolActions.AXE_DIG);
    }
}
