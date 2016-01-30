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
public class CreatePartyCommand implements IPartyCommand {

    private IPartyManager mPartyManager;
    private UUID mExecutorId;

    private static final String ERROR_YOU_CANT_USE_THIS_COMMAND = new FancyMessage(OrangeParty.PREFIX + "Du kannst diesen Command nicht benutzen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_ALLREADY_IN_PARTY = new FancyMessage(OrangeParty.PREFIX + "Du bist bereits in einer Party").color(ChatColor.RED).toJSONString();
    private static final String MSG_PARTY_CREATED = new FancyMessage(OrangeParty.PREFIX + "Party erfolgreich erstellt").toJSONString();

    public CreatePartyCommand(IPartyManager partyManager, UUID executorId) {
        mPartyManager = partyManager;
        mExecutorId = executorId;
    }

    @Override
    public boolean execute() {
        Player executor = Bukkit.getPlayer(mExecutorId);

        // Executor has to have the Permission to execute this Command
        if (!executor.hasPermission(OrangeParty.PERM_CREATE_PARTY)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_CANT_USE_THIS_COMMAND);
            return false;
        }

        // Executor can't be in a Party
        if (!mPartyManager.createParty(mExecutorId)) {
            Utils.sendJsonMsg(executor, ERROR_ALLREADY_IN_PARTY);
            return false;
        }

        // Notify Executor
        Utils.sendJsonMsg(executor, MSG_PARTY_CREATED);
        return true;
    }
}
