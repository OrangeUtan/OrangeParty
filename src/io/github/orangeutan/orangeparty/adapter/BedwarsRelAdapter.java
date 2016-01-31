package io.github.orangeutan.orangeparty.adapter;

import io.github.yannici.bedwars.Game.Game;
import io.github.yannici.bedwars.Game.GameState;
import io.github.yannici.bedwars.Game.Team;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Michael on 31.01.2016.
 */
public class BedwarsRelAdapter implements IMinigameInstance {

    private Game mBedwars;

    public BedwarsRelAdapter(Game bedwars) {
        mBedwars = bedwars;
    }

    @Override
    public boolean hasEnoughSpaceFor(int playerAmount) {
        return mBedwars.getMaxPlayers() - mBedwars.getPlayerAmount() >= playerAmount;
    }

    @Override
    public boolean hasTeams() {
        return true;
    }

    @Override
    public boolean canPlayerJoin(Player player) {
        // Check if the Game has enough free Space for the Player
        if (!hasEnoughSpaceFor(1)) return false;
        // Check if Game is already running or is stopped
        if (mBedwars.getState() == GameState.STOPPED || mBedwars.getState() == GameState.RUNNING) return false;
        // Check internally if Player can join
        return mBedwars.getCycle().onPlayerJoins(player);
    }

    @Override
    public Set<Player> canPlayersJoin(Set<Player> players) {
        // Check if the Game has enough free Space for all Players
        if (!hasEnoughSpaceFor(players.size())) return players;

        Set<Player> canNotJoin = new HashSet<>(players.size());
        for (Player player : players) {
            if (!canPlayerJoin(player)) canNotJoin.add(player);
        }

        return canNotJoin;
    }

    @Override
    public boolean canPlayersJoinAsTeam(Set<Player> players) {
        if (!hasTeams()) return false;
        if(!canPlayersJoin(players).isEmpty()) return false;

        // Check if a Team has enough free Space for all Party Members
        for (Team team: mBedwars.getTeams().values()) {
            if (team.getMaxPlayers() - team.getPlayers().size() >= players.size()) return true;
        }
        return false;
    }

    @Override
    public boolean join(Player player) {
        return mBedwars.playerJoins(player);
    }

    @Override
    public Set<Player> joinAll(Set<Player> players) {
        HashSet<Player> couldNotJoin = new HashSet<>();
        for (Player player : players) {
            if (!mBedwars.playerJoins(player)) couldNotJoin.add(player);
        }
        return couldNotJoin;
    }

    @Override
    public Set<Player> joinAllAsTeam(Set<Player> players) {
        HashSet<Player> couldNotJoin = new HashSet<>();
        // Check if a Team has enough free Space for all Party Members
        for (Team team: mBedwars.getTeams().values()) {
            if (team.getMaxPlayers() - team.getTeamPlayers().size() >= players.size()) {
                for (Player player : players) {
                    // Add the Player to the Team
                    mBedwars.playerJoinTeam(player, team);
                    // Check if the Player was added to the Team
                    if (!team.getPlayers().contains(player)) couldNotJoin.add(player);
                }
                // Return the Players who could not join the Team
                return couldNotJoin;
            }
        }
        return players;
    }

    @Override
    public boolean leave(Player player) {
        return mBedwars.playerLeave(player, false); // False for Player left, true for Player kicked
    }

    @Override
    public Set<Player> leaveAll(Set<Player> players) {
        HashSet<Player> couldNotLeave = new HashSet<>();
        for (Player player : players) {
            if (!mBedwars.playerLeave(player, false)) couldNotLeave.add(player);
        }
        return couldNotLeave;
    }
}
