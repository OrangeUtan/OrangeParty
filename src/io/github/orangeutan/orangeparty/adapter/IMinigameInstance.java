package io.github.orangeutan.orangeparty.adapter;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Interface to interact with any kind of Minigame
 */
public interface IMinigameInstance {

    /**
     * Check if the {@link Player}s can join the Minigame
     * @param players
     * @return
     */
    boolean canPlayersJoin(Set<Player> players);

    /**
     * Add a {@link Player} to the Minigame
     * @param player The {@link Player} who should be added to the Minigame
     * @return true if the {@link Player} was added to the Minigame, false otherwise
     */
    boolean join(Player player);

    /**
     * Add all {@link Player}s in the {@link Set} to the Minigame
     * @param players The {@link Player}s which should be added to the Minigame
     * @return A {@link Set} of Players who failed to join the Minigame
     */
    Set<Player> joinAll(Set<Player> players);

    /**
     * Remove a {@link Player} from the Minigame
     * @param player The {@link Player} who should be remove from the Minigame
     * @return true if the {@link Player} was removed from the Minigame, false otherwise
     */
    boolean leave(Player player);

    /**
     * Remove all {@link Player}s in the {@link Set} from the Minigame
     * @param players The {@link Player}s who should be remove from the Minigame
     * @return A {@link Set} of Players who couldn't be remove from the Minigame
     */
    Set<Player> leaveAll(Set<Player> players);
}