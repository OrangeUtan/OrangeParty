package io.github.orangeutan.orangeparty.adapter;

import io.github.yannici.bedwars.Game.Team;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Michael on 31.01.2016.
 */
public class BedwarsRelTeamAdapter implements IMinigameTeam {

    private BedwarsRelAdapter mBedwars;
    private Team mTeam;

    public BedwarsRelTeamAdapter(BedwarsRelAdapter bedwars, Team team) {
        mBedwars = bedwars;
        mTeam = team;
    }

    @Override
    public boolean addPlayer(Player player) {
        return mTeam.addPlayer(player);
    }

    @Override
    public Set<Player> addAllPlayers(Set<Player> players) {
        Set<Player> couldNotBeAdded = new HashSet<>();
        for (Player player : players) {
            if (!addPlayer(player)) couldNotBeAdded.add(player);
        }
        return couldNotBeAdded;
    }

    @Override
    public boolean removePlayer(Player player) {
        mTeam.removePlayer(player);
        return mTeam.isInTeam(player);
    }

    @Override
    public Set<Player> removeAllPlayers(Set<Player> players) {
        Set<Player> couldNotBeRemoved = new HashSet<>();
        for (Player player : players) {
            if (!removePlayer(player)) couldNotBeRemoved.add(player);
        }
        return couldNotBeRemoved;
    }
}