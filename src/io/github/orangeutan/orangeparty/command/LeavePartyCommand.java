package io.github.orangeutan.orangeparty.command;

import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.party.IPartyManager;
import io.github.orangeutan.orangeparty.utils.Utils;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Michael on 29.01.2016.
 */
public class LeavePartyCommand implements IPartyCommand {

    private IPartyManager mPartyManager;
    private UUID mExecutor;

    private static final String ERROR_YOU_CANT_USE_THIS_COMMAND = new FancyMessage(OrangeParty.PREFIX + "Du kannst diesen Command nicht benutzen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_NOT_IN_A_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du bist in keiner Party").color(ChatColor.RED).toJSONString();

    private static final String MSG_YOU_LEFT_THE_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du hast die Party verlassen").toJSONString();
    private static final String MSG_PLAYER_LEFT_YOUR_PARTY = new FancyMessage(OrangeParty.PREFIX + "Der Spieler")
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("hat deine Party verlassen").toJSONString();
    private static final String MSG_PARTY_WAS_REMOVED = new FancyMessage(OrangeParty.PREFIX + "Die Party wurde vom Owner aufgelöst").toJSONString();

    public LeavePartyCommand(IPartyManager partyManager, UUID executor) {
        mPartyManager = partyManager;
        mExecutor = executor;
    }

    @Override
    public boolean execute() {
        Player executor = Bukkit.getPlayer(mExecutor);

        // Executor has to have the Permission to execute this Command
        if (!executor.hasPermission(OrangeParty.PERM_LEAVE_PARTY)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_CANT_USE_THIS_COMMAND);
            return false;
        }

        // Executor has to be in a Party to leave it
        UUID partyId = mPartyManager.inParty(mExecutor);
        if (partyId == null) {
            Utils.sendJsonMsg(executor, ERROR_NOT_IN_A_PARTY);
            return false;
        }

        // Notify the Members of the Party that the Player left the Party
        mPartyManager.broadcastJsonMsg(partyId, String.format(MSG_PLAYER_LEFT_YOUR_PARTY, executor.getName()));

        // If the Executor is the Owner of the Party it will get deleted
        if (mPartyManager.isOwner(partyId, mExecutor)) {
            // Notify the Members of the Party that the Party was removed
            mPartyManager.broadcastJsonMsg(partyId, MSG_PARTY_WAS_REMOVED);
        }

        // Finally Executor leaves the Party (Party will get removed if the Executor is the Owner of teh Party)
        mPartyManager.leaveParty(partyId, mExecutor);

        //Notify the Executor
        Utils.sendJsonMsg(executor, MSG_YOU_LEFT_THE_PARTY);
        return true;
    }
}
