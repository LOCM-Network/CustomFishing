package me.phuongaz.fishing;

import cn.nukkit.plugin.PluginBase;

public class Loader extends PluginBase{

    private static Loader _instance;

    @Override
    public void onLoad(){
        _instance = this;
    }

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        saveDefaultConfig();
    }

    public static Loader getInstance(){
        return _instance;
    }
}
