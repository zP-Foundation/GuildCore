package zorg.bchru.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class console {
    public static void echo(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + message
        );
    }
}
