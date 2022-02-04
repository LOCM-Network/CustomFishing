package me.phuongaz.fishing;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import me.phuongaz.fishing.session.PlayerSession;
import me.phuongaz.fishing.utils.Utils;


public class Loader extends PluginBase{

    private static Loader _instance;
    private static PlayerSession sessions;

    @Override
    public void onLoad(){
        _instance = this;
    }

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getCommandMap().register("CustomFishing", new FishShopCommand());
        Utils.checkDate();
        saveDefaultConfig();
        sessions = new PlayerSession();
    }

    public static Loader getInstance(){
        return _instance;
    }

    public static PlayerSession getSession(){
        return sessions;
    }

    public Config getSessionConfig(){
        return new Config(getDataFolder() + "/players.yml");
    }

}
