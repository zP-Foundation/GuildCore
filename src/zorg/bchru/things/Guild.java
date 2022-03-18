package zorg.bchru.things;

import org.bukkit.entity.Player;
import zorg.bchru.Interfaces.quarry;

public class Guild {
    static String ID;
    static String Name;
    static String Motd;
    static String JoinMessage;
    static Player[] Members = new Player[1337];
    static quarry qu;



    public void init(quarry Qu){
        qu = Qu;
    }
}
