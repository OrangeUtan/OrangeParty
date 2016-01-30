package io.github.orangeutan.orangeparty.menus;

import io.github.orangeutan.orangeitemmenu.items.NavigateItem;
import io.github.orangeutan.orangeitemmenu.menus.IItemMenu;
import io.github.orangeutan.orangeitemmenu.menus.StaticMenu;
import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.menus.item.PartyMemberKickItem;
import io.github.orangeutan.orangeparty.menus.item.PartyMemberTeleportItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by Michael on 28.01.2016.
 */
public class PartyMemberMenu extends StaticMenu {

    private UUID mMember;

    public PartyMemberMenu(Plugin plugin, Player player, UUID member, IItemMenu parentMenu) {
        super(plugin, player, Size.ONE_LINE, Bukkit.getOfflinePlayer(member).getName(), parentMenu);
        mMember = member;

        addItem(new NavigateItem(this, "Zurück", null));
        if (mPlayer.hasPermission(OrangeParty.PERM_TELEPORT_TO_MEMBER)) addItem(new PartyMemberTeleportItem(this, mMember));

        UUID party = ((OrangeParty)mPlugin).getPartyManager().inParty(mPlayer.getUniqueId());
        if (party != null && ((OrangeParty)mPlugin).getPartyManager().isOwner(party, mPlayer.getUniqueId())) {
            addItem(new PartyMemberKickItem(this, mMember));
        }
    }
}
