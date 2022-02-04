package me.phuongaz.fishing.form;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;
import me.phuongaz.fishing.Loader;
import me.phuongaz.fishing.api.CustomFishingAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.ModalForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.Toggle;

public class FormStorage {

    public static void sendMainForm(Player player){
        SimpleForm form = new SimpleForm(TextFormat.colorize("&l&0CỬA HÀNG HẢI SẢN"));
        form.addButton(TextFormat.colorize("&L&2Mua thêm lượt câu"), (p, button) -> {
            sendSlotShop(p);
        });
        form.addButton(TextFormat.colorize("&l&2Bán vật phẩm câu được"), (p, button) -> {
            sendShopForm(p);
        });

        form.addButton(TextFormat.colorize("&l&2Nâng cấp cần câu"), (p, button) -> {
            sendUpgradeForm(p);
        });
        form.send(player);
    }

    public static void sendSlotShop(Player player){
        int price = Loader.getInstance().getConfig().getInt("price");
        CustomForm form = new CustomForm(TextFormat.colorize("&l&eCửa hàng câu cá"));
        form.addElement(TextFormat.colorize("&l&fGiá: &e" + price +" &7/&e 1&f lượt"));
        form.addElement("soluong", new Input(TextFormat.colorize("&l&fSố lượng cần mua:")));
        form.addElement("mua", new Toggle(TextFormat.colorize("&l&fXác nhận mua:")));
        form.setHandler((p, response) -> {
            boolean mua = response.getToggle("mua").getValue();
            if(mua){
                try{
                    int amount = Integer.parseInt(response.getInput("soluong").getValue());
                    if(EconomyAPI.getInstance().myMoney(p) >= price * amount){
                        EconomyAPI.getInstance().reduceMoney(p, price * amount);
                        p.sendMessage(TextFormat.colorize("&l&fMua thành công &e" + amount + "&f lượt câu trong hôm nay"));
                        CustomFishingAPI.addMax(player, amount);
                    }else{
                        p.sendMessage(TextFormat.colorize("&l&eKhông đủ xu, cần &c" + price * amount + " &exu"));
                    }
                }catch (NumberFormatException e){
                    p.sendMessage(TextFormat.colorize("&l&cSai số lượng"));
                }
            }
        });
        form.send(player);
    }

    public static void sendShopForm(Player player){
        CustomForm form = new CustomForm(TextFormat.colorize("&l&eCỬA HÀNG HẢI SẢN"));
        Item item = player.getInventory().getItemInHand();
        int kg = CustomFishingAPI.getKg(item);
        int price = kg * 100;
        String content = "";
        if(kg == 0){
            if(item.getId() == 0){
                content = "&l&fBạn cần cầm vật phẩm cần bán trên tay!";
            }else {
                price = 1;
                content = "&lVật phẩm này bán ở đây không có giá, bạn có muốn bán nó với giá &e1&f xu";
            }
        }else{
            if(CustomFishingAPI.isFish(item)){
                if(kg < 50){
                    content = "&l&fCá này á, còn hơi bé, nó chỉ có &e" + kg + "&f kg nên bán được có &e" + price + " &fxu thôi, bạn có muốn bán không ?";
                }
                if(kg > 50 && kg < 150){
                    content = "&l&fHmmm, con này &e" + kg + "&f kg à củng tạm, bạn có muốn bán cho tôi với giá &e" + price + "&f xu không?";
                }
                if(kg > 150){
                    content = "&l&eWow, con này lớn đấy, tận &e" + kg + " &fkg bán tôi đi, tôi mua nó với giá &e" + price + "&e xu!";
                }
            }
        }
        form.addElement(new Label(TextFormat.colorize(content)));
        form.addElement("xacnhan", new Toggle(TextFormat.colorize("&l&fXác nhận mua!")));
        int finalPrice = price;
        form.setHandler((p , response) -> {
            if(response.getToggle("xacnhan").getValue()){
                p.getInventory().setItemInHand(Item.get(0));
                EconomyAPI.getInstance().addMoney(p, finalPrice);
                p.sendMessage(TextFormat.colorize("&l&fBán thành công với giá " + finalPrice + " &fxu"));
            }
        });
        form.send(player);
    }

    public static void sendUpgradeForm(Player player){
        CustomForm form = new CustomForm(TextFormat.colorize("&l&eCỬA HÀNG HẢI SẢN"));
        Item item = player.getInventory().getItemInHand();
        int next_level = 1;
        int price = 2000;
        String content = "";
        if(item.getId() == ItemID.FISHING_ROD){
            if(item.hasCompoundTag() && item.getNamedTag().contains("FishLevel")){
                int current = item.getNamedTag().getInt("FishLevel");
                price *= current;
                if(current >= 301){
                    content = "&l&eĐã đạt cấp độ tối đa";
                }else{
                    next_level += current;
                }
            }
            content = "&l&fBạn có muốn nâng cấp cần câu lên cấp độ &e" + next_level + "\n" +
                    "&fGiá nâng cấp: &e" + price + "&f xu\n" +
                    "&fCần câu có cấp độ càng cao càng câu được cá to!";
        }else{
            content = "&l&eChỉ có thể nâng cấp cần câu!";
        }
        form.addElement(new Label(TextFormat.colorize(content)));
        form.addElement("cancan", new Toggle(TextFormat.colorize("&l&fXác nhận!")));
        int finalPrice = price;
        int finalNext_level = next_level;
        form.setHandler((p, response) -> {
            if(response.getToggle("cancan").getValue() && finalNext_level < 301 && item.getId() == ItemID.FISHING_ROD){
                EconomyAPI ecoapi = EconomyAPI.getInstance();
                if(ecoapi.myMoney(p) >= finalPrice){
                    CompoundTag nbt = new CompoundTag();
                    nbt.putInt("FishLevel", finalNext_level);
                    p.sendMessage(TextFormat.colorize("&l&eNâng cấp thành công"));
                    ecoapi.reduceMoney(p, finalPrice);
                    p.getInventory().setItemInHand(item.setNamedTag(nbt)
                            .setCustomName(TextFormat.colorize("&l&fCần câu &a- &fCấp Độ&e " + finalNext_level)));
                }
            }
        });
        form.send(player);
    }
}
