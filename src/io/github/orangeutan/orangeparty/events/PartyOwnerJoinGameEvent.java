package io.github.orangeutan.orangeparty.events;

import io.github.orangeutan.orangeparty.adapter.IMinigameInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Michael on 30.01.2016.
 */
public class PartyOwnerJoinGameEvent extends Event implements Cancellable{

    private boolean mIsCancelled = false;
    private static final HandlerList handlers = new HandlerList();
    private Player mOwner;
    private UUID mPartyId;
    private IMinigameInstance mMinigame;

    public PartyOwnerJoinGameEvent(Player owner, UUID partyId, IMinigameInstance minigame) {
        mOwner = owner;
        mPartyId = partyId;
        mMinigame = minigame;
    }

    public Player getOwner() {
        return mOwner;
    }

    public UUID getPartyId() {
        return mPartyId;
    }

    public IMinigameInstance getMinigame() {
        return mMinigame;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        mIsCancelled = isCancelled;
    }
}