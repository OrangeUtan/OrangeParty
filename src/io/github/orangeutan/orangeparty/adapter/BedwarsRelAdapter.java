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
    public IMinigameTeam canPlayersJoinAsTeam(Set<Player> players) {
        if (!hasTeams()) return null;
        if(!canPlayersJoin(players).isEmpty()) return null;

        // Check if a Team has enough free Space for all Party Members
        for (Team team: mBedwars.getTeams().values()) {
            // Return the Team if it has enough free Space for all Party Members
            if (team.getMaxPlayers() - team.getPlayers().size() >= players.size()) return new BedwarsRelTeamAdapter(this, team);
        }
        // Return null if no Team with enough free Space was found
        return null;
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
    public Set<Player> joinAllAsTeam(IMinigameTeam team, Set<Player> players) {
        // If the Team has not enough free Space for all Players, return all Players
        if (!team.hasEnoughFreeSpaceFor(players.size())) return players;
        // Add all Players to the Minigame. Save the Players who could not join
        HashSet<Player> couldNotJoin = (HashSet<Player>) joinAll(players);
        // Remove all Players who could not join the Game
        players.removeAll(couldNotJoin);
        // Add only the Players who could join to the Team
        team.addAllPlayers(players);
        // Return the Player who could not join the Minigame
        return couldNotJoin;
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
