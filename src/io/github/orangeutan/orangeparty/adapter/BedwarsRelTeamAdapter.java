package io.github.orangeutan.orangeparty.adapter;

import io.github.yannici.bedwars.Game.Team;
import org.bukkit.entity.Player;

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
    public boolean removePlayer(Player player) {
        mTeam.removePlayer(player);
        return mTeam.isInTeam(player);
    }
}