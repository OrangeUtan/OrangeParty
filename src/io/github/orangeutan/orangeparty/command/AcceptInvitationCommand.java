package io.github.orangeutan.orangeparty.command;

import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.executors.PartyExecutor;
import io.github.orangeutan.orangeparty.IPartyManager;
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
public class AcceptInvitationCommand implements IPartyCommand {

    private IPartyManager mPartyManager;
    private UUID mExecuterId;
    private UUID mPartyId;

    private static final String ERROR_YOU_CANT_USE_THIS_COMMAND = new FancyMessage(OrangeParty.PREFIX + "Du kannst diesen Command nicht benutzen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_NOT_INVITED_TO_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du wurdest nicht in die Party von")
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("eingeladen").toJSONString();
    private static final String ERROR_SENDER_ALREADY_IN_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du bist schon in einer Party. Willst du stattdessen deine Party verlassen?")
                                                                    .then(" /" + PartyExecutor.command + " " + PartyExecutor.OP_LEAVE_PARTY).color(ChatColor.GOLD).command("/" + PartyExecutor.command + " " + PartyExecutor.OP_LEAVE_PARTY)
                                                                    .toJSONString();
    private static final String ERROR_ACCEPT_FAILED = new FancyMessage(OrangeParty.PREFIX + "Der Befehl ist fehlgeschlagen").color(ChatColor.RED).toJSONString();

    private static final String MSG_YOU_JOINED_THE_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du bist der Party von")
                                                            .then(" %s ").color(ChatColor.GOLD)
                                                            .then("beigetreten").toJSONString();
    private static final String MSG_PLAYER_JOINED_YOUR_PARTY = new FancyMessage(OrangeParty.PREFIX + "Der Spieler")
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("ist deiner Party beigetreten").toJSONString();

    public AcceptInvitationCommand(IPartyManager partyManager, UUID executer, UUID partyId) {
        mPartyManager = partyManager;
        mExecuterId = executer;
        mPartyId = partyId;
    }

    @Override
    public boolean execute() {
        Player executor = Bukkit.getPlayer(mExecuterId);

        // Executor has to have the Permission to execute this Command
        if (!executor.hasPermission(OrangeParty.PERM_ACCEPT_INVITE)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_CANT_USE_THIS_COMMAND);
            return false;
        }

        OfflinePlayer targetPartyLeader = Bukkit.getOfflinePlayer(mPartyId);
        // Executor has to be invited to the Party
        if (!mPartyManager.isInvitedToParty(mPartyId, mExecuterId)) {
            Utils.sendJsonMsg(executor, String.format(ERROR_NOT_INVITED_TO_PARTY, targetPartyLeader.getName()));
            return false;
        }

        // Executor can't be in a Party
        if (mPartyManager.inParty(mExecuterId) != null) {
            Utils.sendJsonMsg(executor, ERROR_SENDER_ALREADY_IN_PARTY);
            return false;
        }

        // Finally accept the Invitation
        if (!mPartyManager.acceptInvitation(mPartyId, mExecuterId)) Utils.sendJsonMsg(executor, ERROR_ACCEPT_FAILED);
        // Add the Executor to the Party
        if (!mPartyManager.joinParty(mPartyId, mExecuterId)) Utils.sendJsonMsg(executor, ERROR_ACCEPT_FAILED);
        // Notify Executor
        Utils.sendJsonMsg(executor, String.format(MSG_YOU_JOINED_THE_PARTY, targetPartyLeader.getName()));
        // Notify the Party Members that the Executor joined their Party
        mPartyManager.broadcastJsonMsg(mPartyId, String.format(MSG_PLAYER_JOINED_YOUR_PARTY, executor.getName()));
        return true;
    }
}