package io.github.orangeutan.orangeparty.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Michael on 28.01.2016.
 */
public class PlayerUtils {

    /**
     * Gibt des entsprechenden Player mit dem Namen <i>playerName</i><br>
     * zurueck.<br>
     * Falls kein solcher Player existiert, wird null zurueckgegeben.
     *
     * @param playerName (String)
     * @return Player Objekt (oder null)
     */
    public static Player getPlayer(String playerName){
        Object[] playerObjects = Bukkit.getServer().getOnlinePlayers().toArray();

        for(Object playerObject : playerObjects){
            Player player = (Player) playerObject;
            if(player.getName().toLowerCase().equals(playerName.toLowerCase())){
                return player;
            }
        }

        return null;
    }

    /**
     * Gibt des entsprechenden OfflinePlayer mit dem Namen <i>playerName</i><br>
     * zurueck.<br>
     * Falls kein solcher OfflinePlayer existiert, wird null zurueckgegeben.
     *
     * @param playerName (String)
     * @return OfflinePlayer Objekt (oder null)
     */
    public static OfflinePlayer getOfflinePlayer(String playerName){
        OfflinePlayer[] offlinePlayers = Bukkit.getServer().getOfflinePlayers();

        for(OfflinePlayer aktuellerPlayer : offlinePlayers){
            if(aktuellerPlayer.getName().toLowerCase().equals(playerName.toLowerCase())){
                return aktuellerPlayer;
            }
        }

        return null;
    }

    /**
     * Gibt die UUID eines Players mit dem Namen <i>playerName</i><br>
     * zurueck. <br>
     * Falls kein solcher Player existiert, wird null zurueckgegeben.
     *
     * @param playerName
     * @return UUID des Players
     */
    public static UUID getUUID(String playerName){
        Player p = getPlayer(playerName);

        if(p != null){
            return getPlayer(playerName).getUniqueId();
        }

        return null;
    }

}