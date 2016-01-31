package io.github.orangeutan.orangeparty.adapter;

import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Interface to interact with any kind of Minigame
 */
public interface IMinigameInstance {

    /**
     * Check if the Game has enough free Space for the entered Amount of Players
     * @param playerAmount The Amount of Players
     * @return true if Game has enough free Space, false otherwise
     */
    boolean hasEnoughSpaceFor(int playerAmount);

    /**
     * Check if the Game supports Teams
     * @return true if Game supports Teams, false otherwise
     */
    boolean hasTeams();

    /**
     * Check if the {@link Player} can join the Minigame. Checks if the Game is already running, is stopped, has enough space, etc.
     * @param player The {@link Player}s to check if they can join the Minigame
     * @return true if the {@link Player} can join the Minigame, false otherwise
     */
    boolean canPlayerJoin(Player player);

    /**
     * Check if all the {@link Player}s in the {@link Set} can join the Minigame. Checks if the Game is already running, is stopped, has enough space, etc.
     * @param players The {@link Player}s to check if they can join the Minigame
     * @return A {@link Set} of the {@link Player}s who can not join the Minigame
     */
    Set<Player> canPlayersJoin(Set<Player> players);

    /**
     * Get a {@link IMinigameTeam} in which all the {@link Player}s in the {@link Set} can join
     * @param players The {@link Player}s to check if they can join the Game as a Team
     * @return A {@link IMinigameTeam} in which all {@link Player}s can join, or null if no {@link IMinigameTeam} with enough free Space was found
     */
    IMinigameTeam canPlayersJoinAsTeam(Set<Player> players);

    /**
     * Add a {@link Player} to the Minigame
     * @param player The {@link Player} who should be added to the Minigame
     * @return true if the {@link Player} was added to the Minigame, false otherwise
     */
    boolean join(Player player);

    /**
     * Add all {@link Player}s in the {@link Set} to the Minigame
     * @param players The {@link Player}s which should be added to the Minigame
     * @return A {@link Set} of the Players who failed to join the Minigame
     */
    Set<Player> joinAll(Set<Player> players);

    /**
     * Add all {@link Player}s in the {@link Set} to the Minigame as a Team
     * @param players The {@link Player}s which should be added to the Minigame as a Team
     * @return A {@link Set} of the Players who failed to join the Minigame as a Team
     */
    Set<Player> joinAllAsTeam(Set<Player> players);

    /**
     * Remove a {@link Player} from the Minigame
     * @param player The {@link Player} who should be remove from the Minigame
     * @return true if the {@link Player} was removed from the Minigame, false otherwise
     */
    boolean leave(Player player);

    /**
     * Remove all {@link Player}s in the {@link Set} from the Minigame
     * @param players The {@link Player}s who should be remove from the Minigame
     * @return A {@link Set} of the Players who couldn't be remove from the Minigame
     */
    Set<Player> leaveAll(Set<Player> players);
}