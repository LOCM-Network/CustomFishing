package me.phuongaz.fishing.api;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import me.phuongaz.fishing.Loader;

import java.util.*;

public class CustomFishingAPI {

    public static int getPlayerAmount(Player player){
        return Loader.getSession().getCurrentAmount(player);
    }

    public static void setPlayerAmount(Player player, int amount){
        Loader.getSession().setAmount(player, amount);
    }

    public static void addPlayerAmount(Player player, int amount){
        Loader.getSession().addAmount(player, amount);
    }

    public static void reducePlayerAmount(Player player, int amount){
        Loader.getSession().reduceAmount(player, amount);
    }

    public static int getMax(Player player){
        return Loader.getSession().getMaxAmount(player);
    }

    public static void addMax(Player player, int amount){
        Loader.getSession().addMax(player, amount);
    }

    public static int getFishingLevel(Item item){
        int level = 0;
        if(item.getId() == ItemID.FISHING_ROD){
            if(item.hasCompoundTag() && item.getNamedTag().exist("FishLevel")){
                level = item.getNamedTag().getInt("FishLevel");
            }
        }

        return level;
    }

    public static int randKgByLevel(int level, Item reward){
        Random random = new Random();
        int kg = random.nextInt(10);
        if(isFish(reward)){
            kg += random.nextInt((level * random.nextInt(10)) + random.nextInt(level));
        }
        return kg;
    }

    public static int getKg(Item item){
        int kg = 0;
        if(item.hasCompoundTag() && item.getNamedTag().exist("kg")){
           kg += item.getNamedTag().getInt("kg");
        }
        return kg;
    }

    public static Item getRewardByLevel(int level, Item rod){
        Map<Item, String> rewards = getRewards();
        List<Item> i_rewards = new ArrayList<>(rewards.keySet());
        Random random = new Random();
        Item reward = i_rewards.get(random.nextInt(i_rewards.size()));
        int kg = randKgByLevel(level, rod);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("kg", kg);
        reward.setNamedTag(nbt);
        reward.setCustomName(TextFormat.colorize(rewards.get(reward)));
        reward.setLore(TextFormat.colorize("&l&fCân nặng: &e " + kg + "\n&l&fBạn có thể bán cá tại &e/fishshop"));
        return reward;
    }


    public static Map<Item, String> getRewards(){
        Map<Item, String> rewards = new HashMap<>();
        rewards.put(Item.get(ItemID.RAW_FISH), "&l&eCá Sống");
        rewards.put(Item.get(ItemID.RAW_FISH), "&l&eCá chà bặc");
        rewards.put(Item.get(ItemID.CLOWNFISH), "&l&eCá Hề");
        rewards.put(Item.get(ItemID.PUFFERFISH), "&l&eCá nóc");
        rewards.put(Item.get(ItemID.ARROW), "&l&fCung tên rỉ sắt");
        rewards.put(Item.get(ItemID.DIAMOND), "&bKiêm Cương");
        return rewards;
    }

    public static boolean isFish(Item item){
        return (item.getId() == ItemID.RAW_FISH || item.getId() == ItemID.CLOWNFISH || item.getId() == ItemID.PUFFERFISH);
    }
}
