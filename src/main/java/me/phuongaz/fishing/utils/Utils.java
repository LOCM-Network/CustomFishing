package me.phuongaz.fishing.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import me.phuongaz.fishing.Loader;

public class Utils {
    
    public static boolean chance(int percen){
        Random rd = new Random();
        int all = rd.nextInt(10000);
        return (all >= 1 && all < percen * 100);
    }

    public static Map<Integer, Item> getLoots(){
        Map<Integer, Item> loots = new HashMap<>();
        Config config = Loader.getInstance().getConfig();
        config.getSection("customs").getAll().getKeys(false).forEach(key -> {
            int id = config.getInt("customs." + key + ".id");
            int meta = config.getInt("customs." + key + ".meta");
            Item item = Item.get(id, meta);
            if(config.exists("customs." + key + ".name")){
                item.setCustomName(TextFormat.colorize(config.getString("customs." + key + ".name")));
            }
            if(config.exists("customs." + key + ".lore")){
                //item.setLore((String[])config.getStringList("customs." + key + ".lore").toArray());
            }
            loots.put(config.getInt("customs." + key + ".chance"), item);
        });
        return loots;
    }

}
