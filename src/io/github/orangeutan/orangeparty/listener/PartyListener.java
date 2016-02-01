package io.github.orangeutan.orangeparty.listener;

import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.adapter.BedwarsRelAdapter;
import io.github.orangeutan.orangeparty.adapter.IMinigameTeam;
import io.github.orangeutan.orangeparty.adapter.MobArenaAdapter;
import io.github.orangeutan.orangeparty.events.PartyLeaderJoinGameEvent;
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
    private static final String ERROR_YOU_COULD_NOT_FOLLOW_THE_PARTY_LEADER = new FancyMessage(OrangeParty.PREFIX + "Du konntest dem Party Leader").color(ChatColor.RED)
                                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                                .then("nicht ins Minigame folgen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_CAN_NOT_FOLLOW_THE_PARTY_LEADER = new FancyMessage(OrangeParty.PREFIX + "Du kannst dem Party Leader").color(ChatColor.RED)
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
            if (mPlugin.getPartyManager().isLeader(partyId, event.getPlayer().getUniqueId())) {
                mPlugin.getPartyManager().removeParty(event.getPlayer().getUniqueId());
                mPlugin.getPartyManager().broadcastJsonMsg(partyId, MSG_YOUR_PARTY_WAS_CANCELED);
            } else {
                mPlugin.getPartyManager().leaveParty(partyId, event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerJoinsMobArena(ArenaPlayerJoinEvent event) { // Called when a Player joins a MobArena
        // Get the Party in which the Player is in
        UUID partyId = mPlugin.getPartyManager().inParty(event.getPlayer().getUniqueId());
        // Check if the Player is in a Party
        if (partyId != null) {
            // Player has to be Leader of the Party
            if (mPlugin.getPartyManager().isLeader(partyId, event.getPlayer().getUniqueId())) {
                // Call a new PartyLeaderJoinGameEvent to let the
                PartyLeaderJoinGameEvent partyLeaderJoinGameEvent = new PartyLeaderJoinGameEvent(event.getPlayer(), partyId, new MobArenaAdapter(event.getArena()));
                Bukkit.getServer().getPluginManager().callEvent(partyLeaderJoinGameEvent);

                // Cancel the Player joining the MobArena if the PartyLeaderJoinGameEvent gets cancelled and the associated Configuration is set to true
                if (partyLeaderJoinGameEvent.isCancelled() && mPlugin.getConfig().getBoolean(OrangeParty.CFG_AUTOJOIN_MA_CANCEL_LEADER_JOIN_WHEN_MEMBERS_CANT_JOIN)) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinsBedwars(BedwarsPlayerJoinEvent event) {
        // Get the Party in which the Player is in
        UUID partyId = mPlugin.getPartyManager().inParty(event.getPlayer().getUniqueId());
        // Check if the Player is in a Party
        if (partyId != null) {
            // Player has to be Leader of the Party
            if (mPlugin.getPartyManager().isLeader(partyId, event.getPlayer().getUniqueId())) {
                // Call a new PartyLeaderJoinGameEvent to let the
                PartyLeaderJoinGameEvent partyLeaderJoinGameEvent = new PartyLeaderJoinGameEvent(event.getPlayer(), partyId, new BedwarsRelAdapter(event.getGame()));
                Bukkit.getServer().getPluginManager().callEvent(partyLeaderJoinGameEvent);

                // Cancel the Player joining the MobArena if the PartyLeaderJoinGameEvent gets cancelled and the associated Configuration is set to true
                if (partyLeaderJoinGameEvent.isCancelled() && mPlugin.getConfig().getBoolean(OrangeParty.CFG_AUTOJOIN_MA_CANCEL_LEADER_JOIN_WHEN_MEMBERS_CANT_JOIN)) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPartyLeaderJoinsGame(PartyLeaderJoinGameEvent event) {

        // Check if the Leader can join the Minigame
        if (!event.getMinigame().canPlayerJoin(event.getLeader())) {
            event.setCancelled(true);
            return;
        }

        Set<Player> partyMembersMinusLeader = new HashSet<>();
        // Get the online Party Members of the Party Leader, Leader excluded
        for (UUID memberId : mPlugin.getPartyManager().getPartyMembersOf(event.getLeader().getUniqueId())) {
            OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(memberId);
            // The Party Member has to be online to join the Minigame
            if (offlineMember.isOnline()) partyMembersMinusLeader.add((Player) offlineMember);
        }

        // Check if all Party Members can join the Minigame
        Set<Player> canNotJoin = event.getMinigame().canPlayersJoin(partyMembersMinusLeader);
        // Check if some Party Members can not join the Minigame
        if (!canNotJoin.isEmpty()) {
            String canNotJoinString = StringUtils.join(Utils.getPlayerNames(canNotJoin), ",");
            // Notify the Party Leader
            Utils.sendJsonMsg(event.getLeader(), String.format(ERROR_PLAYERS_CAN_NOT_FOLLOW, canNotJoinString));
            // Notify the Members who can not join the Minigame
            Utils.sendJsonMsg(canNotJoin, String.format(ERROR_YOU_CAN_NOT_FOLLOW_THE_PARTY_LEADER, event.getLeader().getName()));
            // Cancel the Leader joning the Minigame if the Configuration is set
            if (mPlugin.getConfig().getBoolean(OrangeParty.CFG_AUTOJOIN_MA_CANCEL_LEADER_JOIN_WHEN_MEMBERS_CANT_JOIN)) {
                event.setCancelled(true);
                return;
            }
            // Remove the Members who can not join the Minigame
            partyMembersMinusLeader.remove(canNotJoin.toArray());
        }

        Set<Player> couldNotJoin = new HashSet<>();
        // Check if the Minigame supports Teams and the Party can join as a Team
        if (event.getMinigame().hasTeams()) {
            Set<Player> partyMembersPlusLeader = new HashSet<>(partyMembersMinusLeader);
            partyMembersPlusLeader.add(event.getLeader());

            // Get a Team in which all Players (Plus the Party Leader) can join
            IMinigameTeam team = event.getMinigame().canPlayersJoinAsTeam(partyMembersPlusLeader);
            if (team!= null) {
                // Add all Party Members (Minus the Party Leader) to the Minigame as a Team. Save the Members who could not join the Team
                couldNotJoin = event.getMinigame().joinAllAsTeam(team, partyMembersMinusLeader);
                // Add the Party Leader to the team
                team.addPlayer(event.getLeader());
            }
        } else {
            // Add all Party Members to the Minigame. Save the Members who could not join the Minigame
            couldNotJoin = event.getMinigame().joinAll(partyMembersMinusLeader);
        }

        if (!couldNotJoin.isEmpty()) {
            String couldNotJoinString = StringUtils.join(Utils.getPlayerNames(couldNotJoin), ",");
            Utils.sendJsonMsg(event.getLeader(), String.format(ERROR_PLAYERS_COULD_NOT_FOLLOW, couldNotJoinString));
            Utils.sendJsonMsg(couldNotJoin, String.format(ERROR_YOU_COULD_NOT_FOLLOW_THE_PARTY_LEADER, event.getLeader().getName()));
        }
    }
}