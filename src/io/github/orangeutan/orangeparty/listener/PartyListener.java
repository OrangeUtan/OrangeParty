package io.github.orangeutan.orangeparty.listener;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.commands.Commands;
import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import io.github.orangeutan.orangeparty.OrangeParty;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 29.01.2016.
 */
public class PartyListener implements Listener {

    private OrangeParty mPlugin;

    private static final String MSG_PLAYER_LEFT_YOUR_PARTY = new FancyMessage("Der Spieler")
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("hat deine Party verlassen").toJSONString();
    private static final String MSG_YOUR_PARTY_WAS_CANCELED = "Deine Party wurde aufgelöst";

    public PartyListener(OrangeParty plugin) {
        mPlugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) { // Called when Player quits, gets kicked or gets banned
        UUID partyId = mPlugin.getPartyManager().inParty(event.getPlayer().getUniqueId());
        if (partyId != null) {
            mPlugin.getPartyManager().broadcastJsonMsg(partyId, String.format(MSG_PLAYER_LEFT_YOUR_PARTY, event.getPlayer().getName()));
            if (mPlugin.getPartyManager().isOwner(partyId, event.getPlayer().getUniqueId())) {
                mPlugin.getPartyManager().removeParty(event.getPlayer().getUniqueId());
                mPlugin.getPartyManager().broadcastJsonMsg(partyId, MSG_YOUR_PARTY_WAS_CANCELED);
            } else {
                mPlugin.getPartyManager().leaveParty(partyId, event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerJoinMobArena(ArenaPlayerJoinEvent event) {
        UUID partyId = mPlugin.getPartyManager().inParty(event.getPlayer().getUniqueId());
        if (partyId != null) {
            // Player has to be Owner
            if (mPlugin.getPartyManager().isOwner(partyId, event.getPlayer().getUniqueId())) {
                Set<UUID> members = mPlugin.getPartyManager().getPartyMembersOf(event.getPlayer().getUniqueId());
                for (UUID member : members) {
                    Player player = Bukkit.getPlayer(member);
                    event.getArena().playerJoin(player, event.getPlayer().getLocation());
                }
            }
        }
    }
}