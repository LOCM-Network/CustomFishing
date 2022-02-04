package me.phuongaz.fishing;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import me.phuongaz.fishing.form.FormStorage;

public class FishShopCommand extends Command {

    public FishShopCommand(){
        super("fishshop", "Open fishing shop");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player) FormStorage.sendMainForm(((Player) sender));
        return false;
    }
}
