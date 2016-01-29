package io.github.orangeutan.orangeparty.party;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 27.01.2016.
 */
public interface IParty {

    static final int DEFAULT_CAPACITY = 5;

    boolean addMember(UUID uuid);

    boolean removeMember(UUID uuid);

    boolean isFull();

    boolean isInParty(UUID player);

    UUID getOwner();

    Set<UUID> getMembers();

    boolean isOwner(UUID player);
}