package net.rezolv.obsidanum.chests.client.model;

import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.block.IronChestsTypes;
import org.jetbrains.annotations.NotNull;

public class IronChestsModels {

  public static final ResourceLocation OBSIDIAN_CHEST_LOCATION = new ResourceLocation(Obsidanum.MOD_ID, "model/obsidian_chest");
  public static final ResourceLocation RUNIC_OBSIDIAN_CHEST_LOCATION = new ResourceLocation(Obsidanum.MOD_ID, "model/runic_obsidian_chest");
  public static final ResourceLocation VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");


  public static final ResourceLocation TRAPPED_VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/trapped");

  public static ResourceLocation chooseChestTexture(IronChestsTypes type) {

      return getResourceLocation(type,  OBSIDIAN_CHEST_LOCATION, RUNIC_OBSIDIAN_CHEST_LOCATION,  VANILLA_CHEST_LOCATION);

  }

  @NotNull
  private static ResourceLocation getResourceLocation(IronChestsTypes type, ResourceLocation obsidianChestLocation,ResourceLocation runicObsidianChestLocation, ResourceLocation vanillaChestLocation) {
    return switch (type) {
      case OBSIDIAN -> obsidianChestLocation;
      case RUNIC_OBSIDIAN -> runicObsidianChestLocation;
      default -> vanillaChestLocation;
    };
  }
}
