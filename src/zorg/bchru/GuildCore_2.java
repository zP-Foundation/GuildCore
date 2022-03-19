package zorg.bchru;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import zorg.bchru.Interfaces.quarry;
import zorg.bchru.things.Guild;

import java.sql.SQLException;


public class GuildCore_2 extends JavaPlugin implements Listener {

    static quarry querry = new quarry();
    static Guild[] Guilds = new Guild[1337];

    public static void GuildCore_2(){

    }

    @Override
    public void onEnable() {
        try {
            querry.init();

            querry.droptables();
            querry.initTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender interpreter, Command cmd, String input, String[] args) {

        Player player = (Player) interpreter;

        if (!input.equalsIgnoreCase(input)) {
            input = input.toLowerCase();
        }
        if (input.equals("g")) {
            if (args.length >= 0) {
                try {
                    querry.sendMessagetoGuild(player, args.toString());
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage("You need to join a guild to use guild chat");
            }
        }
        return true;
    }
}


