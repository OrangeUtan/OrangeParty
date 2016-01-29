package io.github.orangeutan.orangeparty.executor;

import io.github.orangeutan.orangeparty.OrangeParty;
import io.github.orangeutan.orangeparty.command.*;
import io.github.orangeutan.orangeparty.utils.PlayerUtils;
import io.github.orangeutan.orangeparty.utils.Utils;
import io.github.orangeutan.orangeparty.menu.PartyMenu;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Michael on 27.01.2016.
 */
public class PartyExecutor implements CommandExecutor {

    public static final String command = "orange-party";
    private OrangeParty plugin;

    public static final String OP_CREATE_PARTY = "create";
    public static final String OP_LEAVE_PARTY = "leave";
    public static final String OP_INVITE_TO_PARTY = "invite";
    public static final String OP_ACCEPT_INVITATION = "accept";
    public static final String OP_KICK = "kick";

    private static final String ERROR_SENDER_MUST_BE_PLAYER = "Du musst ein Spieler sein um diesen Befehl auszuführen";
    private static final String ERROR_NO_PLAYER_ENTERED = "Du musst einen Spielernamen eingeben";
    private static final String ERROR_PLAYER_DOES_NOT_EXIST = new FancyMessage("Der Spieler").color(ChatColor.RED)
                                                                .then(" %s ").color(ChatColor.GOLD)
                                                                .then("existiert nicht oder ist nicht online").color(ChatColor.RED).toJSONString();

    public PartyExecutor(OrangeParty plugin) {
        this.plugin = plugin;
        plugin.getCommand(command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        for (int i=0; i<args.length; i++) args[i] = args[i].toLowerCase();

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ERROR_SENDER_MUST_BE_PLAYER);
            return false;
        }
        Player sender = (Player)commandSender;

        if (args.length < 1) {
            return openPartyMenu(sender);
        }
        String operator = args[0];

        switch (operator) {
            case OP_CREATE_PARTY:
                return createParty(sender);
            case OP_LEAVE_PARTY:
                return leaveParty(sender);
            case OP_INVITE_TO_PARTY:
                return inviteToParty(sender, Arrays.copyOfRange(args, 1, args.length));
            case OP_ACCEPT_INVITATION:
                return acceptInvitation(sender, Arrays.copyOfRange(args, 1, args.length));
            case OP_KICK:
                return kickMember(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    private boolean openPartyMenu(Player sender) {
        if (plugin.getPartyManager().inParty((sender).getUniqueId()) != null) {
            new PartyMenu(plugin, sender).open(null);
            return true;
        } else {
            sender.sendMessage("Du bist in keiner Party");
        }
        return false;
    }

    private boolean createParty(Player sender) {
        return new CreatePartyCommand(plugin.getPartyManager(), sender.getUniqueId()).execute();
    }

    private boolean leaveParty(Player sender) {
        return new LeavePartyCommand(plugin.getPartyManager(), sender.getUniqueId()).execute();
    }

    private boolean inviteToParty(Player sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ERROR_NO_PLAYER_ENTERED);
            return false;
        }

        Player targetPlayer = PlayerUtils.getPlayer(args[0]);
        // Target Player has to be online to be invited
        if (targetPlayer == null || !targetPlayer.isOnline()){
            Utils.sendJsonMsg(sender, String.format(ERROR_PLAYER_DOES_NOT_EXIST, args[0]));
            return false;
        }
        return new InvitePlayerCommand(plugin.getPartyManager(), sender.getUniqueId(), targetPlayer.getUniqueId()).execute();
    }

    private boolean acceptInvitation(Player sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ERROR_NO_PLAYER_ENTERED);
            return false;
        }

        Player targetPartyMember = PlayerUtils.getPlayer(args[0]);
        // Target Player has to be online to be invited
        if (targetPartyMember == null){
            Utils.sendJsonMsg(sender, String.format(ERROR_PLAYER_DOES_NOT_EXIST, args[0]));
            return false;
        }
        return new AcceptInvitationCommand(plugin.getPartyManager(), sender.getUniqueId(), targetPartyMember.getUniqueId()).execute();
    }

    private boolean kickMember(Player sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ERROR_NO_PLAYER_ENTERED);
            return false;
        }
        return new KickMemberCommand(plugin.getPartyManager(), sender.getUniqueId(), args[0]).execute();
    }
}