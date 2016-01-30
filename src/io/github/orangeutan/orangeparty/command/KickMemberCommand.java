package io.github.orangeutan.orangeparty.command;

import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.IPartyManager;
import io.github.orangeutan.orangeparty.utils.PlayerUtils;
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
public class KickMemberCommand implements IPartyCommand {

    private IPartyManager mPartyManager;
    private String mMemberName;
    private UUID mExecutorId;

    private static final String ERROR_YOU_CANT_USE_THIS_COMMAND = new FancyMessage(OrangeParty.PREFIX + "Du kannst diesen Command nicht benutzen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_YOU_HAVE_TO_BE_OWNER = new FancyMessage(OrangeParty.PREFIX + "Du musst Owner der Party sein um diesen Command auszuführen").color(ChatColor.RED).toJSONString();
    private static final String ERROR_PLAYER_IS_NOT_IN_PARTY = new FancyMessage(OrangeParty.PREFIX + "Der Spieler").color(ChatColor.RED)
                                                                    .then(" %s ").color(ChatColor.GOLD)
                                                                    .then("ist nicht in deiner Party").color(ChatColor.RED).toJSONString();

    private static final String MSG_YOU_WERE_KICKED = new FancyMessage(OrangeParty.PREFIX + "Du wurdest aus der Party von").color(ChatColor.RED)
                                                        .then(" %s ").color(ChatColor.GOLD)
                                                        .then("gekickt").color(ChatColor.RED).toJSONString();
    private static final String MSG_MEMBER_WAS_KICKED = new FancyMessage(OrangeParty.PREFIX + "Spieler")
                                                            .then(" %s ").color(ChatColor.GOLD)
                                                            .then("wurde aus deiner Party gekickt").toJSONString();


    public KickMemberCommand(IPartyManager partyManager, UUID executorId, String memberName) {
        mPartyManager = partyManager;
        mExecutorId = executorId;
        mMemberName = memberName;
    }

    @Override
    public boolean execute() {
        Player executor = Bukkit.getPlayer(mExecutorId);

        // Executor has to have the Permission to execute this Command
        if (!executor.hasPermission(OrangeParty.PERM_KICK_MEMBER)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_CANT_USE_THIS_COMMAND);
            return false;
        }

        // Executor has to be Owner of the Party to execute this Command. Owner UUID = PartyId
        if (!mPartyManager.isOwner(mExecutorId, mExecutorId)) {
            Utils.sendJsonMsg(executor, ERROR_YOU_HAVE_TO_BE_OWNER);
            return false;
        }

        // Member has to be Party to be kicked
        OfflinePlayer member = PlayerUtils.getOfflinePlayer(mMemberName);
        if (member == null || !mPartyManager.isInParty(mExecutorId, member.getUniqueId())) {
            Utils.sendJsonMsg(executor, String.format(ERROR_PLAYER_IS_NOT_IN_PARTY, mMemberName));
            return false;
        }

        // Finally kick the Player
        mPartyManager.leaveParty(mExecutorId, member.getUniqueId());
        // Inform the Player that he was kicked
        Utils.sendJsonMsg(member, String.format(MSG_YOU_WERE_KICKED, executor.getName()));
        // Inform the Party that the Player was kicked
        mPartyManager.broadcastJsonMsg(mExecutorId, String.format(MSG_MEMBER_WAS_KICKED, mMemberName));
        return true;
    }
}
