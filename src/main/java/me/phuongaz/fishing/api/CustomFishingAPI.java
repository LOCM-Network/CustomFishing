package me.phuongaz.fishing.api;

import cn.nukkit.Player;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.food.Food;
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
            System.out.println("\nis fish\n");
            kg += random.nextInt(level) * random.nextInt(3);
        }
        return kg;
    }

    public static int getKg(Item item){
        int kg = 0;
        if(item.hasCompoundTag() && item.getNamedTag().exist("kg")){
            System.out.println(item.getNamedTag().getInt("kg"));
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
        rewards.put(Item.get(ItemID.GOLD_INGOT), "&eVàng");
        rewards.put(Item.get(ItemID.IRON_INGOT), "&fSắt");
        rewards.put(Item.get(ItemID.EMERALD), "&aNgọc Lục Bảo");
        rewards.put(Item.get(ItemID.COAL), "&8Than");
        rewards.put(Item.get(ItemID.REDSTONE), "&cĐá Đỏ");
        rewards.put(Item.get(ItemID.STICK), "&fCây gậy");
        rewards.put(Item.get(ItemID.STRING), "&fSợi dây");
        rewards.put(Item.get(ItemID.BONE), "&fXương");
        rewards.put(Item.get(ItemID.ROTTEN_FLESH), "&fThịt thối");
        rewards.put(Item.get(ItemID.LEATHER), "&fDa");
        rewards.put(Item.get(ItemID.FEATHER), "&fLông vũ");
        rewards.put(Item.get(ItemID.SLIMEBALL), "&aBóng dính");
        rewards.put(Item.get(ItemID.GUNPOWDER), "&fBột súng");
        rewards.put(Item.get(BlockID.GOLD_BLOCK), "&eKhối Vàng");
        rewards.put(Item.get(BlockID.IRON_BLOCK), "&fKhối Sắt");
        rewards.put(Item.get(BlockID.DIAMOND_ORE), "&bMỏ Kim Cương");
        rewards.put(Item.get(BlockID.EMERALD_ORE), "&aMỏ Ngọc Lục Bảo");
        rewards.put(Item.get(BlockID.REDSTONE_ORE), "&cMỏ Đá Đỏ");
        rewards.put(Item.get(BlockID.COAL_ORE), "&8Mỏ Than");
        rewards.put(Item.get(BlockID.LAPIS_ORE), "&9Mỏ Lapis");

        Enchantment[] enchantments = {
                Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL),
                Enchantment.getEnchantment(Enchantment.ID_PROTECTION_FIRE),
                Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY)
        };
        for (Enchantment enchantment : enchantments) {
            int level = new Random().nextInt(2);
            Item item = Item.get(ItemID.BOOK);
            item.setCustomName(TextFormat.colorize("&l&fSách Phép: &e" + enchantment.getName() + " " + level));
            item.addEnchantment(enchantment.setLevel(level));
            rewards.put(item, "&l&fSách Phép: &e" + enchantment.getName() + " " + level);
        }

        //seeds
        Item[] seeds = {
                Item.get(ItemID.WHEAT_SEEDS),
                Item.get(ItemID.PUMPKIN_SEEDS),
                Item.get(ItemID.MELON_SEEDS),
                Item.get(ItemID.BEETROOT_SEEDS)
        };

        for (Item seed : seeds) {
            rewards.put(seed, "&l&fHạt giống: &e" + seed.getName());
        }

        return rewards;
    }

    public static boolean isFish(Item item){
        return (item.getId() == ItemID.RAW_FISH || item.getId() == ItemID.CLOWNFISH || item.getId() == ItemID.PUFFERFISH);
    }
}
