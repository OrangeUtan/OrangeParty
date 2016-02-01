package io.github.orangeutan.orangeparty;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 27.01.2016.
 */
public class Party implements IParty {

    private HashSet<UUID> mMembers = new HashSet<>();
    private UUID mLeader;
    private int mCapacity;

    public Party(UUID leader, int capacity) {
        mLeader = leader;
        mCapacity = capacity;
    }

    public boolean addMember(UUID uuid) {
        if (!isFull() && !isInParty(uuid)) return mMembers.add(uuid);
        return false;
    }

    public boolean removeMember(UUID uuid) {
        return mMembers.remove(uuid);
    }

    @Override
    public boolean isFull() {
        return mMembers.size() == mCapacity;
    }

    @Override
    public boolean isInParty(UUID player) {
        if (player == mLeader) return true;
        return mMembers.contains(player);
    }

    @Override
    public UUID getLeader() {
        return mLeader;
    }

    @Override
    public Set<UUID> getMembers() {
        Set<UUID> members = mMembers;
        members.add(mLeader);
        return members;
    }

    @Override
    public boolean isLeader(UUID player) {
        return mLeader == player;
    }
}
