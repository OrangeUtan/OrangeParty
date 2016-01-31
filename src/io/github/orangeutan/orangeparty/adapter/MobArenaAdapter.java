package io.github.orangeutan.orangeparty.adapter;

import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Michael on 30.01.2016.
 */
public class MobArenaAdapter implements IMinigameInstance {

    private Arena mArena;

    public MobArenaAdapter(Arena arena) {
        mArena = arena;
    }

    @Override
    public boolean hasEnoughSpaceFor(int playerAmount) {
        return mArena.getMaxPlayers() - mArena.getPlayerCount() >= playerAmount;
    }

    @Override
    public boolean hasTeams() {
        // MobArena has no Team Support
        return false;
    }

    @Override
    public boolean canPlayerJoin(Player player) {
        return mArena.canJoin(player);
    }

    @Override
    public Set<Player> canPlayersJoin(Set<Player> players) {
        HashSet<Player> playersWhoCanNotJoin = new HashSet<>();
        for (Player player : players) {
            if (!mArena.canJoin(player)) playersWhoCanNotJoin.add(player);
        }
        return playersWhoCanNotJoin;
    }

    @Override
    public IMinigameTeam canPlayersJoinAsTeam(Set<Player> players) {
        // MobArena has no Team Support
        return null;
    }

    @Override
    public boolean join(Player player) {
        if (mArena.canJoin(player)) {
            mArena.playerJoin(player, player.getLocation());
        }
        return false;
    }

    @Override
    public Set<Player> joinAll(Set<Player> players) {
        HashSet<Player> playersWhoCouldNotJoin = new HashSet<>();
        for (Player player : players) {
            if (!mArena.playerJoin(player, player.getLocation())) playersWhoCouldNotJoin.add(player);
        }
        return playersWhoCouldNotJoin;
    }

    @Override
    public boolean leave(Player player) {
        return mArena.playerLeave(player);
    }

    @Override
    public Set<Player> leaveAll(Set<Player> players) {
        HashSet<Player> playersWhoCouldNotLeave = new HashSet<>();
        for (Player player : players) {
            if (!mArena.playerLeave(player)) playersWhoCouldNotLeave.add(player);
        }
        return playersWhoCouldNotLeave;
    }
}