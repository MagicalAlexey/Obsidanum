package net.rezolv.obsidanum.chests.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.rezolv.obsidanum.chests.block.entity.IronChestsBlockEntityTypes;
import net.rezolv.obsidanum.chests.block.entity.ObsidianChestBlockEntity;

import javax.annotation.Nullable;

public class ObsidianChestBlock extends AbstractIronChestBlock {

  public ObsidianChestBlock(Properties properties) {
    super(properties, IronChestsBlockEntityTypes.OBSIDIAN_CHEST::get, IronChestsTypes.OBSIDIAN);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ObsidianChestBlockEntity(blockPos, blockState);
  }
}
