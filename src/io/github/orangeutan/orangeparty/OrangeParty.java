package io.github.orangeutan.orangeparty;

import com.garbagemule.MobArena.MobArena;
import io.github.orangeutan.orangeitemmenu.MenuListener;
import io.github.orangeutan.orangeparty.executors.PartyExecutor;
import io.github.orangeutan.orangeparty.listener.PartyListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Michael on 27.01.2016.
 */
public class OrangeParty extends JavaPlugin {

    public static final String PREFIX = "[OrangeParty]";
    public static final String PERM_ALL = "orange-party.*";
    public static final String PERM_CREATE_PARTY = "orange-party.create";
    public static final String PERM_TELEPORT_TO_MEMBER = "orange-party.teleport";
    public static final String PERM_KICK_MEMBER = "orange-party.kick";
    public static final String PERM_INVITE_PLAYER = "orange-party.invite";
    public static final String PERM_LEAVE_PARTY = "orange-party.leave";
    public static final String PERM_ACCEPT_INVITE = "orange-party.accept";

    private PartyManager mPartyManager;
    private MobArena mMobArena;

    @Override
    public void onEnable() {
        super.onEnable();

        setUpItemMenuSupport();
        setUpMobArenaSupport();
        setUpConfig();

        new PartyExecutor(this);
        new PartyListener(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void setUpItemMenuSupport() {
        MenuListener.getInstance().register(this);
        mPartyManager = new PartyManager();
    }

    private void setUpMobArenaSupport() {
        mMobArena = (MobArena) getServer().getPluginManager().getPlugin("MobArena");
    }

    private void setUpConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public PartyManager getPartyManager() {
        return mPartyManager;
    }

    public MobArena getMobArena() {
        return mMobArena;
    }
}