package net.rezolv.obsidanum.chests.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.block.IronChestsTypes;
import net.rezolv.obsidanum.chests.block.entity.ObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.block.entity.RunicObsidianChestBlockEntity;
import net.rezolv.obsidanum.chests.client.model.inventory.IronChestItemStackRenderer;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IronChestBlockItem extends BlockItem {

  protected Supplier<IronChestsTypes> type;

  protected Supplier<Boolean> trapped;

  public IronChestBlockItem(Block block, Properties properties, Supplier<Callable<IronChestsTypes>> type) {
    super(block, properties);

    IronChestsTypes tempType = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, type);

    this.type = tempType == null ? null : () -> tempType;
  }

  @Override
  public void initializeClient(Consumer<IClientItemExtensions> consumer) {
    super.initializeClient(consumer);

    consumer.accept(new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        Supplier<BlockEntity> modelToUse;

          switch (type.get()) {
            case RUNIC_OBSIDIAN -> modelToUse = () -> new RunicObsidianChestBlockEntity(BlockPos.ZERO, BlocksObs.RUNIC_OBSIDIAN_CHEST.get().defaultBlockState());

            default -> modelToUse = () -> new ObsidianChestBlockEntity(BlockPos.ZERO, BlocksObs.OBSIDIAN_CHEST.get().defaultBlockState());
          }
        return new IronChestItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), modelToUse);
      }
    });
  }
}
