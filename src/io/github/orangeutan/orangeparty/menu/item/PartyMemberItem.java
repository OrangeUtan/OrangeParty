package io.github.orangeutan.orangeparty.menu.item;

import io.github.orangeutan.orangeitemmenu.items.NavigateItem;
import io.github.orangeutan.orangeitemmenu.menus.IItemMenu;
import io.github.orangeutan.orangeparty.menu.PartyMemberMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * Created by Michael on 28.01.2016.
 */
public class PartyMemberItem extends NavigateItem {
    private UUID mMember;

    public PartyMemberItem(IItemMenu menu, UUID member) {
        super(menu, null, null);
        mMember = member;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(mMember);

        Byte skullType;
        if (offlinePlayer.isOnline()) skullType = (byte)3;
        else {
            skullType = (byte)1;
            String offlineForFormat = "Offline seit: %d Stunden, %d Minuten, %d Sekunden";
            long offlineFor = System.currentTimeMillis() - offlinePlayer.getLastPlayed();

            int seconds = (int) (offlineFor / 1000) % 60 ;
            int minutes = (int) ((offlineFor / (1000*60)) % 60);
            int hours   = (int) ((offlineFor / (1000*60*60)));

            setLore(String.format(offlineForFormat, hours, minutes, seconds));
        }

        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        meta.setOwner(offlinePlayer.getName());
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, skullType);
        skull.setItemMeta(meta);

        setIcon(skull);
        setDisplayName(offlinePlayer.getName());

        IItemMenu newDestination = new PartyMemberMenu(mMenu.getPlugin(), mMenu.getPlayer(), mMember, mMenu);
        setDestination(newDestination);
    }
}