package io.github.orangeutan.orangeparty.adapter;

import org.bukkit.entity.Player;

/**
 * Created by Michael on 31.01.2016.
 */
public interface IMinigameTeam {

    boolean addPlayer(Player player);

    boolean removePlayer(Player player);
}