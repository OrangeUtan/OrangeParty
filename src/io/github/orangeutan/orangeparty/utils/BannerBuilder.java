package io.github.orangeutan.orangeparty.utils;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;

/**
 * Created by Michael on 29.01.2016.
 */
public class BannerBuilder {

    private ItemStack mBanner;
    private BannerMeta mBannerMeta;
    private ArrayList<Pattern> mPatterns;

    public BannerBuilder(DyeColor baseColor) {
        mBanner = new ItemStack(Material.BANNER, 1);
        mBannerMeta = (BannerMeta) mBanner.getItemMeta();

        mBannerMeta.setBaseColor(baseColor);
    }

    public BannerBuilder addPattern(DyeColor color, PatternType patternType) {
        mBannerMeta.addPattern(new Pattern(color, patternType));
        return this;
    }

    public ItemStack build() {
        mBanner.setItemMeta(mBannerMeta);
        return mBanner;
    }
}