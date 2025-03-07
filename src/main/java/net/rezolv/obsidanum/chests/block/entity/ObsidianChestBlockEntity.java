package net.rezolv.obsidanum.chests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.block.IronChestsTypes;
import net.rezolv.obsidanum.chests.inventory.IronChestMenu;

public class ObsidianChestBlockEntity extends AbstractIronChestBlockEntity {

  public ObsidianChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.OBSIDIAN_CHEST.get(), blockPos, blockState, IronChestsTypes.OBSIDIAN, BlocksObs.OBSIDIAN_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createObsidianContainer(containerId, playerInventory, this);
  }
}
