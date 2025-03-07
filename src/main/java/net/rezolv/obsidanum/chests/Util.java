package net.rezolv.obsidanum.chests;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Util {

  public static String toEnglishName(String internalName) {
    return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_"))
      .map(StringUtils::capitalize)
      .collect(Collectors.joining(" "));
  }
}
