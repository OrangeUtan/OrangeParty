package io.github.orangeutan.orangeparty;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 27.01.2016.
 */
public interface IPartyManager {

    UUID inParty(UUID uuid);

    boolean isInParty(UUID partyId, UUID member);

    boolean createParty(UUID leader);

    boolean removeParty(UUID partyId);

    boolean partyExists(UUID partyId);

    IParty getParty(UUID partyId);

    boolean joinParty(UUID partyId, UUID player);

    boolean leaveParty(UUID player);

    boolean leaveParty(UUID partyId, UUID member);

    Set<UUID> getPartyMembersOf(UUID player);

    boolean isOwner(UUID partyId, UUID player);

    void broadcastMsg(UUID partyId, String msg);

    void broadcastJsonMsg(UUID partyId, String msg);

    boolean addInvitation(UUID partyID, UUID player);

    boolean isInvitedToParty(UUID partyId, UUID player);

    boolean acceptInvitation(UUID partyId, UUID player);
}