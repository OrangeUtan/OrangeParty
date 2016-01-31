package io.github.orangeutan.orangeparty.adapter;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Michael on 31.01.2016.
 */
public interface IMinigameTeam {

    /**
     * Add a {@link Player} to the Team
     * @param player The {@link Player} who is to be added to the Team
     * @return True if the {@link Player} was added to the Team, false otherwise
     */
    boolean addPlayer(Player player);

    /**
     * Add all the {@link Player}s in the {@link Set} to the Team
     * @param players The {@link Player}s who are to be added to the Team
     * @return A {@link Set} of the {@link Player}s who could not be added to the Team
     */
    Set<Player> addAllPlayers(Set<Player> players);

    /**
     * Remove a {@link Player} from the Team
     * @param player The {@link Player} who is to be removed from the Team
     * @return True if the {@link Player} was removed from the Team, false otherwise
     */
    boolean removePlayer(Player player);

    /**
     * Remove all the {@link Player}s in the {@link Set} from the Team
     * @param players The {@link Player}s who are to be remove from the Team
     * @return A {@link Set} of the {@link Player}s who could not be removed from the Team
     */
    Set<Player> removeAllPlayers(Set<Player> players);
}