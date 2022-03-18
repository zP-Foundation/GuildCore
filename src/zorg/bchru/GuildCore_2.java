package zorg.bchru;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import zorg.bchru.Interfaces.quarry;
import zorg.bchru.things.Guild;

import java.sql.SQLException;


public class GuildCore_2 extends JavaPlugin implements Listener {

    static quarry querry = new quarry();
    static Guild[] Guilds = new Guild[1337];
    public static void GuildCore_2() throws SQLException {



        //querry.init();
        //querry.initTables();
    }


}
