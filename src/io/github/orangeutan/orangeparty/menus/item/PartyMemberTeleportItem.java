package io.github.orangeutan.orangeparty.menus.item;

import io.github.orangeutan.orangecore.gui.events.ItemClickEvent;
import io.github.orangeutan.orangecore.gui.items.StaticItem;
import io.github.orangeutan.orangecore.gui.menus.IItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Michael on 28.01.2016.
 */
public class PartyMemberTeleportItem extends StaticItem {

    private UUID mMember;

    public PartyMemberTeleportItem(IItemMenu menu, UUID member) {
        super(menu, new ItemStack(Material.ENDER_PEARL, 1), null);
        mMember = member;

        Player target = Bukkit.getPlayer(mMember);
        if (target == null) setDisplayName("Spieler ist nicht online");
        else setDisplayName("Teleportiere dich zu " + target.getName());
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        super.onItemClick(event);

        Player target = Bukkit.getPlayer(mMember);
        if (target != null) {
            event.getPlayer().teleport(target);
            //mMenu.close(event.getPlayer());
        }
    }
}