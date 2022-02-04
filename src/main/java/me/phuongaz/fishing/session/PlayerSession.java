package me.phuongaz.fishing.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import io.sentry.Session;
import me.phuongaz.fishing.Loader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class PlayerSession {

    public static LinkedHashMap<String, Object> sessions = new LinkedHashMap<>();

    public int getCurrentAmount(Player player){
        List<Integer> list = (List<Integer>) sessions.get(player.getName());
        return list.get(0);
    }

    public int getMaxAmount(Player player){
        List<Integer> list = (List<Integer>) sessions.get(player.getName());
        return list.get(1);
    }

    public void setMax(Player player, int amount){
        List<Integer> list = (List<Integer>) sessions.get(player.getName());
        list.set(1, amount);
        sessions.put(player.getName(), list);
    }

    public void setAmount(Player player, int amount){
        List<Integer> list = (List<Integer>) sessions.get(player.getName());
        list.set(0, amount);
        sessions.put(player.getName(), list);
    }

    public void addMax(Player player, int amount){
        setMax(player, amount + getMaxAmount(player));
    }

    public void addAmount(Player player, int amount){
        setAmount(player, getCurrentAmount(player) + amount);
    }

    public void reduceAmount(Player player,int amount){
        setAmount(player, getCurrentAmount(player) - amount);
    }

    public LinkedHashMap<String, Object> getSessions(){
        return sessions;
    }

    public void putSession(Player player, List<Integer> ss){
        sessions.put(player.getName(), ss);
        Loader.getInstance().getLogger().alert("Put session " + player.getName());
    }

    public void removeSession(Player player){
        Config config = Loader.getInstance().getSessionConfig();
        config.set(player.getName() + ".current", getCurrentAmount(player));
        config.set(player.getName() + ".max", getMaxAmount(player));
        config.save();
        config.reload();
        Loader.getInstance().getLogger().alert("Save session " + player.getName());
        sessions.remove(player.getName());
    }

    public void reloadAll(){
        sessions.keySet().forEach(k -> {
            List<Integer> list = (List<Integer>) sessions.get(k);
            list.set(0, 0);
            list.set(1, Loader.getInstance().getConfig().getInt("default"));
            sessions.put(k, list);
        });
        String path = Loader.getInstance().getDataFolder() + "/players.yml";
        File file = new File(path);
        if(file.delete()){
            Loader.getInstance().getLogger().alert("Remove session file!");
        }else{
            Loader.getInstance().getLogger().alert("Remove session file Error!");
        }
        Server.getInstance().broadcastMessage(TextFormat.colorize("&l&eSố lượt Câu đá đã được làm mới!"));
    }

    public void saveAll(){
        Config config = Loader.getInstance().getSessionConfig();
        sessions.keySet().forEach(k -> {
            List<Integer> list = (List<Integer>) sessions.get(k);
            config.set(k + ".current", list.get(0));
            config.set(k + ".max", list.get(1));
        });
        config.save();
    }
}
