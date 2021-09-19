package me.phuongaz.fishing;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFishEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.TextFormat;
import me.phuongaz.fishing.utils.Utils;

public class EventListener implements Listener{
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event){
        boolean hasloot = false;
        Player player = event.getPlayer();
        Map<Integer, Item> loots = Utils.getLoots();
        for(int chance : loots.keySet()){
            if(Utils.chance(chance) && !hasloot){
                Item item = loots.get(chance);
                String name = item.hasCustomName() ? item.getCustomName() : item.getName();
                event.setLoot(item);
                player.sendActionBar(TextFormat.colorize("&a&lBạn vừa câu được:&f " + name + " &f|&e " + chance + "%%"));
                hasloot = true;
            }
        }
    }

}
