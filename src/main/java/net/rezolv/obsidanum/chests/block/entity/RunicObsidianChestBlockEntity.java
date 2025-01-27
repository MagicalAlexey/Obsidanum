package net.rezolv.obsidanum.chests.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.block.BlocksObs;
import net.rezolv.obsidanum.chests.block.IronChestsTypes;
import net.rezolv.obsidanum.chests.inventory.IronChestMenu;

public class RunicObsidianChestBlockEntity extends AbstractIronChestBlockEntity {

  public RunicObsidianChestBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(IronChestsBlockEntityTypes.RUNIC_OBSIDIAN_CHEST.get(), blockPos, blockState, IronChestsTypes.RUNIC_OBSIDIAN, BlocksObs.RUNIC_OBSIDIAN_CHEST::get);
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return IronChestMenu.createRunicObsidianContainer(containerId, playerInventory, this);
  }
}
