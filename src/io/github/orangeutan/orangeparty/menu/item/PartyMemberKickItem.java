package io.github.orangeutan.orangeparty.menu.item;

import io.github.orangeutan.orangeitemmenu.events.ItemClickEvent;
import io.github.orangeutan.orangeitemmenu.items.StaticItem;
import io.github.orangeutan.orangeitemmenu.menus.IItemMenu;
import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.command.KickMemberCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by Michael on 28.01.2016.
 */
public class PartyMemberKickItem extends StaticItem {

    private UUID mMember;

    public PartyMemberKickItem(IItemMenu menu, UUID member) {
        super(menu, new ItemStack(Material.LEATHER_BOOTS, 1), null);
        mMember = member;

        Player target = Bukkit.getPlayer(mMember);
        if (target == null) setDisplayName("Spieler ist nicht online");
        else setDisplayName("Kicke " + target.getName() + " aus der Party");
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        super.onItemClick(event);

        OfflinePlayer target = Bukkit.getOfflinePlayer(mMember);
        // Try to kick the Member
        if (new KickMemberCommand(((OrangeParty)mMenu.getPlugin()).getPartyManager(), event.getPlayer().getUniqueId(), target.getName()).execute()) {
            // Member was kicked, so go back to the PartyMenu
            mMenu.navigateBack();
        }
    }
}