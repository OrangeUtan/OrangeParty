package io.github.orangeutan.orangeparty.listener;

import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.adapter.MobArenaAdapter;
import io.github.orangeutan.orangeparty.events.PartyOwnerJoinGameEvent;
import io.github.orangeutan.orangeparty.utils.Utils;
import io.github.yannici.bedwars.Events.BedwarsPlayerJoinEvent;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 29.01.2016.
 */
public class PartyListener implements Listener {

    private OrangeParty mPlugin;

    private static final String ERROR_PLAYERS_COULD_NOT_FOLLOW = new FancyMessage(OrangeParty.PREFIX + "Die Partymember").color(ChatColor.RED)
                                                                        .then(" [%s] ").color(ChatColor.GOLD)
                                                                        .then("konnten dem Minigame nicht beitreten").color(ChatColor.RED).toJSONString();
    private static final String ERROR_PLAYERS_CAN_NOT_FOLLOW = new FancyMessage(OrangeParty.PREFIX + "Die Partymember").color(ChatColor.RED)
                                                                    .then(" [%s] ").color(ChatColor.GOLD)
                                                                    .then("können dem Minigame nicht beitreten").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_COULD_NOT_FOLLOW_THE_PARTY_OWNER = new FancyMessage(OrangeParty.PREFIX + "Du konntest dem Party Owner").color(ChatColor.RED)
                                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                                .then("nicht ins Minigame folgen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_CAN_NOT_FOLLOW_THE_PARTY_OWNER = new FancyMessage(OrangeParty.PREFIX + "Du kannst dem Party Owner").color(ChatColor.RED)
            .then(" %s ").color(ChatColor.GOLD)
            .then(" nicht ins Minigame folgen").color(ChatColor.RED).toJSONString();


    private static final String MSG_PLAYER_LEFT_YOUR_PARTY = new FancyMessage(OrangeParty.PREFIX + "Der Spieler")
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("hat deine Party verlassen").toJSONString();
    private static final String MSG_YOUR_PARTY_WAS_CANCELED = OrangeParty.PREFIX + "Deine Party wurde aufgelöst";


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
    public void onPlayerJoinMobArena(ArenaPlayerJoinEvent event) { // Called when a Player joins a MobArena
        // Get the Party in which the Player is in
        UUID partyId = mPlugin.getPartyManager().inParty(event.getPlayer().getUniqueId());
        // Check if the Player is in a Party
        if (partyId != null) {
            // Player has to be Owner of the Party
            if (mPlugin.getPartyManager().isOwner(partyId, event.getPlayer().getUniqueId())) {
                // Call a new PartyOwnerJoinGameEvent to let the
                PartyOwnerJoinGameEvent partyOwnerJoinGameEvent = new PartyOwnerJoinGameEvent(event.getPlayer(), partyId, new MobArenaAdapter(event.getArena()));
                Bukkit.getServer().getPluginManager().callEvent(partyOwnerJoinGameEvent);

                // Cancel the Player joining the MobArena if the PartyOwnerJoinGameEvent gets cancelled and the associated Configuration is set to true
                if (partyOwnerJoinGameEvent.isCancelled() && mPlugin.getConfig().getBoolean(OrangeParty.CFG_AUTOJOIN_MA_CANCEL_OWNER_JOIN_WHEN_MEMBERS_CANT_JOIN)) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPrePartyOwnerJoinsGame(PartyOwnerJoinGameEvent event) {

        // Check if the Owner can join the Minigame
        if (!event.getMinigame().canPlayerJoin(event.getOwner())) {
            event.setCancelled(true);
            return;
        }

        Set<Player> partyMembers = new HashSet<>();
        // Get the online Party Members of the Party Owner
        for (UUID memberId : mPlugin.getPartyManager().getPartyMembersOf(event.getOwner().getUniqueId())) {
            OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(memberId);
            // The Party Member has to be online to join the Minigame
            if (offlineMember.isOnline()) partyMembers.add((Player) offlineMember);
        }

        // Check if all Party Members can join the Minigame
        Set<Player> canNotJoin = event.getMinigame().canPlayersJoin(partyMembers);
        // Check if some Party Members can not join the Minigame
        if (!canNotJoin.isEmpty()) {
            String canNotJoinString = StringUtils.join(Utils.getPlayerNames(canNotJoin), ",");
            // Notify the Party Owner
            Utils.sendJsonMsg(event.getOwner(), String.format(ERROR_PLAYERS_CAN_NOT_FOLLOW, canNotJoinString));
            // Notify the Members who can not join the Minigame
            Utils.sendJsonMsg(canNotJoin, String.format(ERROR_YOU_CAN_NOT_FOLLOW_THE_PARTY_OWNER, event.getOwner().getName()));
            // Cancel the Owner joning the Minigame if the Configuration is set
            if (mPlugin.getConfig().getBoolean(OrangeParty.CFG_AUTOJOIN_MA_CANCEL_OWNER_JOIN_WHEN_MEMBERS_CANT_JOIN)) {
                event.setCancelled(true);
                return;
            }
            // Remove the Members who can not join the Minigame
            partyMembers.remove(canNotJoin.toArray());
        }

        // Add all Party Members to the Minigame. Save the Members who could not join the Minigame
        canNotJoin = event.getMinigame().joinAll(partyMembers);
        if (!canNotJoin.isEmpty()) {
            String couldNotJoinString = StringUtils.join(Utils.getPlayerNames(canNotJoin), ",");
            Utils.sendJsonMsg(event.getOwner(), String.format(ERROR_PLAYERS_COULD_NOT_FOLLOW, couldNotJoinString));
            Utils.sendJsonMsg(canNotJoin, String.format(ERROR_YOU_COULD_NOT_FOLLOW_THE_PARTY_OWNER, event.getOwner().getName()));
        }
    }
}