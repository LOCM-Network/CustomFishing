package me.phuongaz.fishing.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.scheduler.NukkitRunnable;
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
                item.setLore(config.getString("customs." + key + ".lore").replace("{n}", "\n"));
            }
            if(config.exists("custom." + key + ".amount")){
                item.setCount(config.getInt("customs." + key + ".amount"));
            }
            if(config.exists("customs." + key + "enchants")){
                List<String> ids = config.getStringList("customs. " + key + ".enchants");
                for(String i : ids){
                    String[] parts = i.split(":");
                    Enchantment enchantment = Enchantment.getEnchantment(Integer.parseInt(parts[0]));
                    enchantment.setLevel(Integer.parseInt(parts[0]));
                    item.addEnchantment(enchantment);
                }
            }   
            loots.put(config.getInt("customs." + key + ".chance"), item);
        });
        return loots;
    }

    public static void checkDate(){
        new NukkitRunnable(){
            @Override
            public void run(){
                if(!Loader.getInstance().getConfig().getString("date").equals(today())){
                    Config config = Loader.getInstance().getConfig();
                    config.set("date", today());
                    config.save();
                    config.reload();
                    Loader.getSession().reloadAll();
                }
            }
        }.runTaskTimer(Loader.getInstance(), 0, 20 * 60);
    }

    public static String today(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    public static String getNameByKg(int kg){
        String name = "&l&bCá &f[&e";
        if(kg <= 50){
            name += "nhỏ";
        }
        if(kg > 50 && kg <= 150){
            name += "vừa";
        }
        if(kg >= 250){
            name += "lớn";
        }
        return name + " &f]";
    }
}
