package io.github.orangeutan.orangeparty;

import io.github.orangeutan.orangeparty.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 27.01.2016.
 */
public class PartyManager implements IPartyManager {


    private HashMap<UUID, IParty> mParties = new HashMap<>(); // PartyId/LeaderId, Party
    private HashMap<UUID, HashSet<UUID>> mPendingInvites = new HashMap<>(); // Player, Sender/PartyId

    public PartyManager() {
    }

    @Override
    public UUID inParty(UUID player) {
        for (IParty party : mParties.values()) {
            if (party.isInParty(player)) return party.getLeader();
        }
        return null;
    }

    @Override
    public boolean isInParty(UUID partyId, UUID member) {
        IParty party = getParty(partyId);
        if (party == null) return false;
        return party.removeMember(member);
    }

    @Override
    public boolean createParty(UUID leader) {
        if (!mParties.keySet().contains(leader)) {
            IParty party = new Party(leader, IParty.DEFAULT_CAPACITY);
            mParties.put(leader, party);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeParty(UUID partyId) {
        removeAllInvitesFrom(partyId);
        return mParties.remove(partyId) != null;
    }

    @Override
    public boolean partyExists(UUID partyId) {
        return mParties.keySet().contains(partyId);
    }

    @Override
    public IParty getParty(UUID partyId) {
        return mParties.get(partyId);
    }

    @Override
    public boolean joinParty(UUID partyId, UUID player) {
        // Check if Party exists
        if (partyExists(partyId)) {
            // Check if Player is not already in a Party
            for (IParty party : mParties.values()) {
                if (party.isInParty(player)) return false;
            }
            return mParties.get(partyId).addMember(player);
        }
        return false;
    }

    @Override
    public boolean leaveParty(UUID player) {
        UUID partyId = inParty(player);
        if (partyId != null) {
            IParty party = getParty(partyId);
            if (player == party.getLeader()) {
                removeParty(partyId);
            } else {
                getParty(partyId).removeMember(player);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean leaveParty(UUID partyId, UUID member) {
        IParty party = getParty(partyId);
        if (party != null) {
            if (party.isLeader(member)) removeParty(partyId);
            else party.removeMember(member);
            return true;
        }
        return false;
    }

    @Override
    public Set<UUID> getPartyMembersOf(UUID player) {
        UUID partyId = inParty(player);
        if (partyId == null) return null;

        Set<UUID> members = new HashSet<>(getParty(partyId).getMembers());
        members.remove(player);
        return members;
    }

    @Override
    public boolean isLeader(UUID partyId, UUID player) {
        if(partyExists(partyId)) return getParty(partyId).isLeader(player);
        return false;
    }

    @Override
    public void broadcastMsg(UUID partyId, String msg) {
        IParty party = mParties.get(partyId);
        if (party != null) {
            for (UUID partyMember : party.getMembers()) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(partyMember);
                if (member.isOnline()) ((Player)member).sendMessage(msg);
            }
        }
    }

    @Override
    public void broadcastJsonMsg(UUID partyId, String msg) {
        IParty party = mParties.get(partyId);
        if (party != null) {
            for (UUID partyMember : party.getMembers()) {
                OfflinePlayer member = Bukkit.getOfflinePlayer(partyMember);
                if (member.isOnline()) Utils.sendJsonMsg((Player)member, msg);
            }
        }
    }

    @Override
    public boolean addInvitation(UUID partyID, UUID player) {
        HashSet<UUID> invitations = new HashSet<>() ;

        // If the Player is already in the PendingInvites list his current Invites
        if (mPendingInvites.keySet().contains(player)) {
            invitations = mPendingInvites.get(player);
        }

        if (!invitations.add(partyID)) return false; // Invitation was not added, because it already existed
        mPendingInvites.put(player, invitations);
        return true;
    }

    @Override
    public boolean isInvitedToParty(UUID partyId, UUID player) {
        HashSet<UUID> invitations = mPendingInvites.get(player);
        return invitations != null && invitations.contains(partyId);
    }

    @Override
    public boolean acceptInvitation(UUID partyId, UUID player) {
        if (isInvitedToParty(partyId, player)) {
            // Remove the Player from the PendingInvites
            return mPendingInvites.remove(player) != null;
        }
        return false;
    }

    /**
     * Remove all Invites to a Party
     * @param partyId The Id of the Party whose Invites to remove
     * @return True if all Invites were removed, false if there were no Invites
     */
    public boolean removeAllInvitesFrom(UUID partyId) {
        for (UUID player : mPendingInvites.keySet()) {
            if (mPendingInvites.get(player).contains(partyId)) {
                mPendingInvites.get(player).remove(partyId);
                if (mPendingInvites.get(player).isEmpty()) mPendingInvites.remove(player);
                return true;
            }
        }
        return false;
    }
}