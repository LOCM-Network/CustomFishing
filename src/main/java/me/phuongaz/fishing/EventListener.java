package me.phuongaz.fishing;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerFishEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;
import me.phuongaz.fishing.api.CustomFishingAPI;

public class EventListener implements Listener{

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Item item = player.getInventory().getItemInHand();
        if(item.getId() == ItemID.FISHING_ROD){
            if(CustomFishingAPI.getPlayerAmount(player) >= CustomFishingAPI.getMax(player)){
                player.sendActionBar(TextFormat.colorize("&c&lĐã hết lượt câu trong hôm nay"));
                event.setCancelled();
            }
        }
    }
    
    @EventHandler(priority= EventPriority.LOW)
    public void onPlayerFish(PlayerFishEvent event){
        Player player = event.getPlayer();
        if(CustomFishingAPI.getPlayerAmount(player) >= CustomFishingAPI.getMax(player)){
            player.sendActionBar(TextFormat.colorize("&c&lĐã hết lượt câu trong hôm nay"));
            event.setCancelled();
            return;
        }
/*        Map<Integer, Item> loots = Utils.getLoots();
        for(int chance : loots.keySet()){
            if(Utils.chance(chance) && !hasloot){
                Item item = loots.get(chance);
                String name = item.hasCustomName() ? item.getCustomName() : item.getName();
                event.setLoot(item);
                player.sendActionBar(TextFormat.colorize("&a&lBạn vừa câu được:&f " + name + " &f|&e " + chance + "%"));
                if(chance < 5){
                    Server.getInstance().broadcastMessage(TextFormat.colorize("&l&fNguời chơi &e" + player.getName() + " &fvừa câu được vật phẩm hiếm (&e" + name + "&f)"));
                }
                hasloot = true;
                CustomFishingAPI.addPlayerAmount(player, 1);
            }
        }*/
        Item rod = player.getInventory().getItemInHand();
        int level = CustomFishingAPI.getFishingLevel(rod);
        Item reward = CustomFishingAPI.getRewardByLevel(level, rod);
        int kg = CustomFishingAPI.getKg(reward);
        if(kg > 239){
            Server.getInstance().broadcastMessage(
                    TextFormat.colorize("&8&l[&eCâu cá&8] &fNgười chơi &e" + player.getName() + "" +
                            "&fVừa câu được " + reward.getCustomName() + " &fcân nặng &e" + kg + " &fKg"));
        }
        event.setLoot(reward);
        CustomFishingAPI.addPlayerAmount(player, 1);
        player.sendActionBar(TextFormat.colorize("&f&lBạn vừa câu được:&e " + reward.getCustomName() + " &8|&f Cân nặng &e" + kg + " &fkg"));
    }

    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){
                if(player.getInventory().getItemInHand().getId() == ItemID.FISHING_ROD){
                    event.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerLocallyInitializedEvent event){
        Player player = event.getPlayer();
        List<Integer> list = new ArrayList<>();
        int amount = Loader.getInstance().getSessionConfig().getInt(player.getName() + ".current");
        int max = Loader.getInstance().getConfig().getInt("default");
        if(Loader.getInstance().getSessionConfig().exists(player.getName())){
            amount = Loader.getInstance().getSessionConfig().getInt(player.getName() + ".current");
            max = Loader.getInstance().getSessionConfig().getInt(player.getName() + ".max");
        }
        list.add(amount);
        list.add(max);
        Loader.getSession().putSession(player, list);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Loader.getSession().removeSession(event.getPlayer());
    }
}
