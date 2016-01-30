package io.github.orangeutan.orangeparty.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Michael on 29.01.2016.
 */
public class Utils {

    public static void sendJsonMsg(Player player, String msg) {
        if (player != null && player.isOnline()) {
            sendJsonMsg((CraftPlayer)player, msg);
        }
    }

    public static void sendJsonMsg(CraftPlayer player, String msg) {
        if (player != null && player.isOnline()) {
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a(msg);
            PacketPlayOutChat packet = new PacketPlayOutChat(cbc);
            player.getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void sendJsonMsg(UUID playerId, String msg) {
        CraftPlayer player = (CraftPlayer) Bukkit.getPlayer(playerId);
        if (player != null && player.isOnline()) {
            sendJsonMsg(player, msg);
        }
    }

    public static void sendJsonMsg(String playerName, String msg) {
        CraftPlayer player = (CraftPlayer) PlayerUtils.getPlayer(playerName);
        if (player != null && player.isOnline()) {
            sendJsonMsg(player, msg);
        }
    }

    public static void sendJsonMsg(OfflinePlayer player, String msg) {
        if (player != null && player.isOnline()) {
            sendJsonMsg((CraftPlayer)player, msg);
        }
    }

    public static void sendJsonMsg(Set<Player> players, String msg) {
        for (Player player : players) {
            sendJsonMsg(player, msg);
        }
    }

    public static ArrayList<String> getPlayerNames(Set<Player> players) {
        ArrayList<String> memberNames = new ArrayList<>(players.size());
        for (Player member : players) memberNames.add(member.getName());
        return memberNames;
    }
}