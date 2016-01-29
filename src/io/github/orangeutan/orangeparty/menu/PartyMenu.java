package io.github.orangeutan.orangeparty.menu;

import io.github.orangeutan.orangeitemmenu.items.CloseItem;
import io.github.orangeutan.orangeitemmenu.menus.StaticMenu;
import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.party.PartyManager;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 27.01.2016.
 */
public class PartyMenu extends StaticMenu {

    public PartyMenu(Plugin plugin, Player player) {
        super(plugin, player, Size.FOUR_LINE, "Deine Party", null);

        setItem(0, new CloseItem(this, "Schließen", new ItemStack(Material.BARRIER)));

        PartyManager partyManager = ((OrangeParty)mPlugin).getPartyManager();
        Set<UUID> members = partyManager.getPartyMembersOf(mPlayer.getUniqueId());
        if (members != null){
            for (UUID uuid : members) {
                addItem(new PartyMemberItem(this, uuid));
            }
        }
    }

    @Override
    public void updateContent() {
        clear();
        setItem(0, new CloseItem(this, "Schließen", new ItemStack(Material.BARRIER)));

        PartyManager partyManager = ((OrangeParty)mPlugin).getPartyManager();
        Set<UUID> members = partyManager.getPartyMembersOf(mPlayer.getUniqueId());
        if (members != null){
            for (UUID uuid : members) {
                addItem(new PartyMemberItem(this, uuid));
            }
        }

        fillEmptySlots(DyeColor.GRAY);
    }
}