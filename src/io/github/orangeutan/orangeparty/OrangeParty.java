package io.github.orangeutan.orangeparty;

import io.github.orangeutan.orangeitemmenu.MenuListener;
import io.github.orangeutan.orangeparty.executor.PartyExecutor;
import io.github.orangeutan.orangeparty.party.IPartyManager;
import io.github.orangeutan.orangeparty.party.PartyManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by Michael on 27.01.2016.
 */
public class OrangeParty extends JavaPlugin {

    public static final String PERM_ALL = "orange-party.*";
    public static final String PERM_CREATE_PARTY = "orange-party.create";
    public static final String PERM_TELEPORT_TO_MEMBER = "orange-party.teleport";
    public static final String PERM_KICK_MEMBER = "orange-party.kick";
    public static final String PERM_INVITE_PLAYER = "orange-party.invite";
    public static final String PERM_LEAVE_PARTY = "orange-party.leave";
    public static final String PERM_ACCEPT_INVITE = "orange-party.accept";

    private PartyManager mPartyManager;

    @Override
    public void onEnable() {
        super.onEnable();

        MenuListener.getInstance().register(this);

        new PartyExecutor(this);

        mPartyManager = new PartyManager();


        getServer().getServicesManager().register(IPartyManager.class, mPartyManager, this, ServicePriority.Normal);

        new PartyExecutor(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public PartyManager getPartyManager() {
        return mPartyManager;
    }
}