package io.github.orangeutan.orangeparty.adapter;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Michael on 30.01.2016.
 */
public interface IMinigame {

    void join(Player player);

    void joinAll(Set<Player> players);

    void leave(Player player);

    void leaveAll(Set<Player> players);
}