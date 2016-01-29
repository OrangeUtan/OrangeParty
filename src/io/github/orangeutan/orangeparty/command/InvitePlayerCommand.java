package io.github.orangeutan.orangeparty.command;

import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.executor.PartyExecutor;
import io.github.orangeutan.orangeparty.party.IPartyManager;
import io.github.orangeutan.orangeparty.utils.Utils;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Michael on 29.01.2016.
 */
public class InvitePlayerCommand implements IPartyCommand {

    private IPartyManager mPartyManager;
    private UUID mExecutorId;
    private UUID mTargetPlayerId;

    private static final String ERROR_YOU_CANT_USE_THIS_COMMAND = new FancyMessage("Du kannst diesen Command nicht benutzen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_HAVE_TO_BE_IN_A_PARTY = new FancyMessage("Du musst in einer Party sein um diesen Befehl auszuführen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_HAVE_TO_BE_PARTY_OWNER = new FancyMessage("Du musst Owner der Party sein um diesen Command auszuführen").color(ChatColor.RED).toJSONString();

    private static final String MSG_INVITE_WAS_SENT = new FancyMessage("Einladung an")
                                                        .then(" %s ").color(ChatColor.GOLD)
                                                        .then("wurde erfolgreich verschickt").toJSONString();
    private static final String MSG_YOU_WERE_INVITED_BY = new FancyMessage("Du wurdest von ")
                                    .then(" %1$s ").color(ChatColor.GOLD)
                                    .then(" in eine Party Eingeladen. ")
                                    .then("/" + PartyExecutor.command + " " + PartyExecutor.OP_ACCEPT_INVITATION + " %1$s").color(ChatColor.GOLD).command("/" + PartyExecutor.command + " " + PartyExecutor.OP_ACCEPT_INVITATION + " %1$s")
                                    .toJSONString();

    public InvitePlayerCommand(IPartyManager partyManager, UUID executorId, UUID targetPlayerId) {
        mPartyManager = partyManager;
        mExecutorId = executorId;
        mTargetPlayerId = targetPlayerId;
    }

    @Override
    public boolean execute() {
        Player executor = Bukkit.getPlayer(mExecutorId);

        // Executor has to have the Permission to execute this Command
        if (!executor.hasPermission(OrangeParty.PERM_INVITE_PLAYER)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_CANT_USE_THIS_COMMAND);
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(mTargetPlayerId); // TODO May have to test if Player exist and/or is online

        UUID partyId = mPartyManager.inParty(mExecutorId);
        // Executor has to be in a Party to invite a Player
        if (partyId == null) {
            Utils.sendJsonMsg(executor, ERROR_YOU_HAVE_TO_BE_IN_A_PARTY);
            return false;
        }

        // Executor has to be Party Owner to execute this Command
        if (!mPartyManager.isOwner(partyId, mExecutorId)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_HAVE_TO_BE_PARTY_OWNER);
            return false;
        }

        // Finally add the Invitation
        mPartyManager.addInvitation(partyId, mTargetPlayerId);
        // Notify the Executor
        Utils.sendJsonMsg(executor, String.format(MSG_INVITE_WAS_SENT, targetPlayer.getName()));
        // Notify the invited Player
        Utils.sendJsonMsg(targetPlayer, String.format(MSG_YOU_WERE_INVITED_BY, executor.getName()));
        return true;
    }
}
