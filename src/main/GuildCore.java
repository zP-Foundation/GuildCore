/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getPlayer;
/**
 *
 * @author Swere
 */
public class GuildCore extends JavaPlugin implements Listener {
    
    public List<String> guildchat = new ArrayList<String>();
    
    public String host, port, database, username, password;
    static MysqlDataSource data = new MysqlDataSource();
    static Statement stmt;
    static Connection conn;
    
    static Guild WhitePhoenix = new Guild();
    
    static int GuildID;
    static public Player Target;
    static public Map<Player, Long> Pancake = new HashMap<Player, Long>();
    static public HashMap<Integer,String> Flux = new HashMap<Integer, String>();
    static public demon Kartoffel = new demon();
    public void onEnable(){
        
            getLogger().info("Loading Guild Core");
            getLogger().info("Setting up Mysql Connection");
            
            //data.setUser("WPGS");
            //data.setPassword("acf25cc962503d2c81f3ef10cd678434");
            //data.setServerName("localhost");
            //data.setPort(3306);
            //data.setDatabaseName("GuildCore");
            getLogger().info("Database connected");
            
        
        try {
            //conn = (Connection) data.getConnection();
            //stmt = (Statement) conn.createStatement();
            String Address = "localhost";
            String Database = "mc_Autumn";
            String DatabaseUser = "Autumn";
            String DatabaseUserPassword = "Hihihi45";
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + Address + "/" + Database, DatabaseUser, DatabaseUserPassword);

            stmt = (Statement) conn.createStatement();
                //stmt.execute("CREATE TABLE IF NOT EXISTS Guilds (GuildID int PRIMARY KEY NOT NULL AUTO_INCREMENT, GuildName varchar(255), Owner varchar(255),Motd TEXT, created timestamp DEFAULT CURRENT_TIMESTAMP)");
                //stmt.execute("CREATE TABLE IF NOT EXISTS Users (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, UUID varchar(255), GuildID int, GuildRank int, Name varchar(255), invitedTime timestamp DEFAULT CURRENT_TIMESTAMP)");
                //stmt.execute("CREATE TABLE IF NOT EXISTS Invites (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, Inviter varchar(255), Target varchar(255), GuildID int, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
                //stmt.execute("CREATE TABLE IF NOT EXISTS Ranks (RankID int PRIMARY KEY NOT NULL, RankName varchar(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS Boop(BoopID int PRIMARY KEY NOT NULL AUTO_INCREMENT, PlayerID varchar(255), TargetID varchar(255), Time timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS JoinMessages(ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, PlayerID varchar(255), Message TEXT, Time timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Guilds (GuildID int PRIMARY KEY NOT NULL AUTO_INCREMENT, GuildName varchar(255), Owner varchar(255),Motd TEXT, created timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, UUID varchar(255), GuildID int, GuildRank int, Name varchar(255), invitedTime timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Invites (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, Inviter varchar(255), Target varchar(255), GuildID int, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Ranks (RankID int PRIMARY KEY NOT NULL, RankName varchar(255))");
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //WhitePhoenix.setGuildID(1337);
        //WhitePhoenix.setGuildName("White Phoenix");
        
        
    }
    public void onDisable(){
            
        echo("Disabling GuildCore");
        
    }
    @Override
    public boolean onCommand(CommandSender interpreter, Command cmd, String input, String[] args){
        
        try {
            conn = (Connection) data.getConnection();
            stmt = (Statement) conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
            if (!input.equalsIgnoreCase(input)){
                
                input = input.toLowerCase();
            }
        if(interpreter instanceof Player){
            Player player = (Player) interpreter;
            String playerID = player.getUniqueId().toString();



            if (input.equals("g")) {
                if (args.length >= 0) {
                    try {
                        zorg.bchru.Interfaces.quarry querry = new zorg.bchru.Interfaces.quarry();
                        querry.sendMessagetoGuild(player, args.toString());
                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }


        /*
        if(input.equals("chatg")){
            
            player.sendMessage("Guild chat active");
        }*/
        if(input.equals("chatall")){
            
        }
        if(input.equals("glist")){
            
                try {
                    ListGuilds(player);
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if(input.equalsIgnoreCase("gmotd")){
                try {
                    String MOTD = "";
                    for(String a : args){
                        MOTD = MOTD + " " + a;
                    }
                    player.sendMessage(MOTD);
                    ResultSet GuildInfo = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+playerID+"'");
                    if(!GuildInfo.next()){
                        player.sendMessage("You are in no Guild");
                        return false;
                    }
                    int playerPermissionLevel = GuildInfo.getInt("GuildRank");
                    if(playerPermissionLevel < 6){
                        player.sendMessage("You have to be a Officer or Higher to set the Motd");
                        return false;
                    } else {
                        GuildID = GuildInfo.getInt("GuildID");
                        stmt.execute("UPDATE Guilds SET Motd = '"+MOTD+"' WHERE GuildID = " + GuildID);
                        return true;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }
        if(input.equals("gcreate")){
            
            if (args[0] == null) {
                player.sendMessage("/gcreate Guildname");
            }
            String GuildName = "";
            GuildName = args[0];
                try {
                    //Find out if User is already in Guild
                    ResultSet isInGuild = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+player.getUniqueId().toString()+"'");
                    if(isInGuild.next()){
                        player.sendMessage("You are already in a Guild");
                        return false;
                    }
                    //Find out if Guild is already created
                    ResultSet Guildtaken = stmt.executeQuery("SELECT * FROM Guilds WHERE GuildName = '"+GuildName+"'");
                    if(Guildtaken.next()){
                        player.sendMessage("Guild already exists");
                        return false;
                    }
                    //Create Guild and Insert User to new Guild
                    stmt.execute("INSERT INTO Guilds (GuildName, Owner) VALUES ('"+GuildName+"', '"+player.getUniqueId().toString()+"')");
                    ResultSet Guild = stmt.executeQuery("SELECT * FROM Guilds WHERE GuildName = '"+GuildName+"'"); 
                    Guild.next();
                    int GuildID = Guild.getInt("GuildID");// 1 recruit, 2 member, 3 VIP-Member, 4 , 5 Veteran, 6 Officer, 7 Guild Admin, 8 GM, 9 Mod, 10 Admin
                    stmt.execute("INSERT INTO Users (UUID, GuildID, GuildRank, Name) VALUES ('"+player.getUniqueId().toString()+"', "+GuildID+", 8, '"+player.getName()+"')");
                    
                    player.sendMessage("Guild Created with ID "+ GuildID);
                    
                    
                    
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        }
        if(input.equals("ginfo2")){
            WhitePhoenix.printdata(player);
        }
        if(input.equals("ginfo")){
                
            PrintGuildInfo(player);
        }
        if(input.equals("gleave")){
                try {
                    ResultSet OrigUserdata = stmt.executeQuery("SELECT * FROM Users WHERE UUID ='"+player.getUniqueId().toString()+"'");
                    if(!OrigUserdata.next()){ //Check if is in guild
                        player.sendMessage("You aren't in a Guild yet");
                    } else {
                        int GuildID = OrigUserdata.getInt("GuildID");
                        if(countUsersGuild(stmt, GuildID) <= 1){ //Delete guild if it has no more users
                            stmt.execute("DELETE FROM Guilds WHERE GuildID = '"+GuildID+"'");
                        }
                        stmt.execute("DELETE FROM Users WHERE UUID = '"+playerID+"'");
                        player.sendMessage("You left the Guild"); //Remove user from guild
                        
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if(input.equals("gjoin")){ //Admin Join Command
                try {
                    //Find out if User is already in Guild
                    ResultSet isInGuild = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+player.getUniqueId().toString()+"'");
                    if(isInGuild.next()){
                        player.sendMessage("You are already in a Guild");
                        return false;
                    }
                    
                    if(args[0] == null){ //Check if playername is given
                        player.sendMessage(ChatColor.RED + "/gjoin Guildname");
                        return false;
                    } else {
                        String GuildName = args[0];
                        ResultSet GuildInfo = stmt.executeQuery("SELECT * FROM Guilds WHERE GuildName ='"+GuildName+"'");
                        if(!GuildInfo.next()){
                            player.sendMessage("Guild doesn't exist");
                        } else {
                            getLogger().log(Level.INFO, "Adding user {0} to Guild {1}", new Object[]{player.getName(), GuildInfo.getString("GuildName")});
                        stmt.execute("INSERT INTO Users (UUID, GuildID, GuildRank, Name) VALUES ('"+player.getUniqueId().toString()+"', "+GuildInfo.getInt("GuildID")+", 10, '"+player.getName()+"')");
                        
                        
                        }
                        
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if(input.equals("gdelete")){
            if(args[0]==null){
                player.sendMessage("/gdelete GuildName");
            } else {
                try {
                String GuildName = args[0];
                ResultSet GetGuild = stmt.executeQuery("SELECT * FROM Guilds WHERE GuildName = '"+GuildName+"'");
                GuildID = GetGuild.getInt("GuildID");
                if(!GetGuild.next()){
                    player.sendMessage("Guild not Found");
                } else {
                    stmt.execute("DELETE FROM Guilds WHERE GuildID = "+GuildID);
                    stmt.execute("DELETE FROM Users WHERE GuildID = "+GuildID);
                    player.sendMessage("Guild: "+GuildName+" deleted");
                }
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if(input.equals("grename")){
                if (args[0] == null) {
                    player.sendMessage("/grename NewGuildName");
                    return false;
                }
                try {
                    ResultSet GRename = stmt.executeQuery(("SELECT * FROM Users INNER JOIN Guilds ON Users.GuildID = Users.GuildID WHERE UUID = '"+playerID+"'"));
                    if(!GRename.next()){
                        player.sendMessage("You are not in a Guild");
                    } else {
                        if(8 != GRename.getInt("GuildRank")){
                            player.sendMessage("Only the GM can rename a Guild");
                        } else {
                            GuildID = GRename.getInt("GuildID");
                            String NewName = args[0];
                            stmt.execute("UPDATE Guilds SET GuildName = '"+NewName+"' WHERE GuildID = "+GuildID);
                        }
                        
                        
                    }
                    
                    
                   } catch (SQLException ex) {
                Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(input.equals("gpromote")){
                try {
                    int guildIDsender = 1;
                    int guildRanksender = 0;
                    int guildIDreciever = 2;
                    int guildRankreciever = 0;
                    int count = 0;
                    if(args[0] == null ^ args[1] == null){
                        player.sendMessage("/promote playername promotion");
                    }
                    Player p = Bukkit.getPlayer(args[0]);
                    player.sendMessage("Player ausgewählt"+p.getName());
                    
                    //Here Code
                    //Überprüfe Gilden zugehörigkeit
                    ResultSet UserGuild = stmt.executeQuery("SELECT GuildID, GuildRank FROM Users WHERE UUID = '"+playerID+"'");
                    if(!UserGuild.next()){ //Get 1 User
                        player.sendMessage("You are not in a Guild");
                        return false;
                    } else {
                        guildIDsender = UserGuild.getInt("GuildID");
                        guildRanksender = UserGuild.getInt("GuildRank");
                        UserGuild = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+p.getUniqueId().toString()+"'");
                        if(!UserGuild.next()){ //Get 2 User
                            player.sendMessage("Your target is not a valid User");
                            return false;
                        } else {
                        guildIDreciever = UserGuild.getInt("GuildID");
                        guildRankreciever = UserGuild.getInt("GuildRank");
                            if(guildIDreciever != guildIDsender){ //vergleiche Gildenzugehörigkeit
                                player.sendMessage("You and your Target are not in the ame Guild");
                                return false;
                            } else {
                                
                                //Gildenlevel zugehörigkeit
                                if(guildRanksender < guildRankreciever){
                                    p.sendMessage("You aren't permitted to promote this user");
                                } else {
                                    if(guildRanksender < 6) {
                                        p.sendMessage("Your rank is to low to do that");
                                    } else {
                                    
                                
                                    ResultSet RankInfo = stmt.executeQuery("SELECT RankID FROM Ranks WHERE RankName = '"+args[1]+"'");
                                    if(!RankInfo.next()){ //Transferiere Ranknamen zu RankID
                                        player.sendMessage("Rank not found. Valid Ranks ");
                                        RankInfo = stmt.executeQuery("SELECT * FROM Ranks");
                                        while(RankInfo.next()){
                                            player.sendMessage("RankID: " + RankInfo.getString("RankID") + " | " + RankInfo.getString("RankName"));
                                        }
                                        return false;
                                    } else {
                                        int GuildRank = RankInfo.getInt("RankID");
                                        //Update Rank
                                        if(guildRanksender > GuildRank){
                                            stmt.execute("UPDATE Users SET RankID = "+ RankInfo.getString("RankID")+" WHERE UUID = '"+p.getUniqueId().toString()+"'");
                                        }

                                        }
                                    }
                                }
                                
                            }
                        }
                    }
                            
                            } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        if(input.equals("ginvite")){
                try {
                    if(args[0] == null){
                        player.sendMessage("/ginvite playername");
                        return false;
                    }
                    String Targetname = args[0];
                    Player Target = Bukkit.getPlayer(Targetname);
                    ResultSet GuildInfo = stmt.executeQuery("SELECT * FROM Users INNER JOIN Guilds ON Users.GuildID = Guilds.GuildID WHERE UUID = '"+playerID+"'");
                    if(!GuildInfo.next()){
                        player.sendMessage("You are not in a Guild");
                    } else {
                        GuildID = GuildInfo.getInt("GuildID");
                        String GuildName = GuildInfo.getString("GuildName");
                        stmt.execute("INSERT INTO Invites (Inviter, Target, GuildID) VALUES ('"+player.getName()+"', '"+Target.getName()+"', "+GuildID+")");
                        player.sendMessage("Invited: " + Target.getName());
                        Target.sendMessage("You got invited by "+player.getName()+" write /gaccept " + GuildName + " to join " + GuildName);
                    }
                
                
                
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }
        if(input.equals("gaccept")){
                try {
                    
                    if(args[0] == null){
                        player.sendMessage("/gaccept guildname");
                        return false;
                    } else {

                        ResultSet GuildInviteData = stmt.executeQuery("SELECT * FROM Invites INNER JOIN Guilds ON Invites.GuildID = Guilds.GuildID WHERE Target = '"+player.getName()+"' AND GuildName = '"+args[0]+"'");
                        if(!GuildInviteData.next()){
                            player.sendMessage("Invite not found");
                        } else {

                            String GuildName = GuildInviteData.getString("GuildName");
                            GuildID = GuildInviteData.getInt("GuildID");
                            GuildInviteData = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+playerID+"'");
                            if(GuildInviteData.next()){
                                player.sendMessage("You are already in a Guild");
                            } else {

                                player.sendMessage("Joining Guild "+ GuildName);
                                //stmt.execute("DELETE FROM Invites WHERE Target = '"+player.getName()+"' AND GuildName = '"+GuildName+"'");
                                stmt.execute("INSERT INTO Users (UUID,GuildID,GuildRank,Name) VALUES ('"+playerID+"', "+GuildID+", 2, '"+player.getName()+"')");
                                player.sendMessage("Welcome to +" + GuildName);
                                player.sendMessage("User /ginfo to see Information about your new Guild");
                            }
                        }

                    }

            } catch (SQLException ex) {
                Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



/*        if(input.equals("ginvite")){
            
            try {
                if(args[0] == null){ //Check if playername is given
                    player.sendMessage(ChatColor.RED + "Person not found, try /invite playername");
                    return false;
                }
                String Targetname = args[0];
                echo("New Invite by: "+ player.getName().toString());
                ResultSet guildinfo;
                    guildinfo = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+player.getUniqueId().toString()+"'");
                
                if(guildinfo.next()){
                    GuildID = guildinfo.getInt("GuildID");
                    Target = getPlayer(Targetname);
                    Target.sendMessage("New Guild invite by "+player.getName()+" type /gaccept to join");
                    Target.sendMessage("You can only have 1 guild");
                    stmt.execute("INSERT INTO Invites (Inviter, Target, GuildID) VALUES ('"+player.getName()+"', '"+Target.getName()+"', '" + GuildID + "')");
                }
        if(input.equals("gaccept")){
            if(args[0] == null){
                player.sendMessage("/gaccept");
                return false;
            }
            ResultSet Stuff = stmt.executeQuery("SELECT * FROM Invites WHERE Target = '"+player.getName()+"' AND Inviter = '"+args[0]+"'");
            if(!Stuff.next()){
                player.sendMessage("Invite not Found");
            } else {
                GuildID = Stuff.getInt(port)
                player.sendMessage("Joining Guild = "+GuildID);
                stmt.execute("DELETE FROM Invites WHERE Target = '"+player.getName()+"' AND Inviter = '"+args[0]+"'");
                stmt.execute("DELETE FROM Users WHERE UUID = '"+player.getUniqueId().toString()+"'");
                stmt.execute("INSERT INTO Users (UUID,GuildID, GuildRank, Name) VALUES ('"+player.getUniqueId().toString()+"', '"+Stuff.getInt("GuildID")+"', 2, '"+player.getName()+"')");
                player.sendMessage("Guild Joined");
            }
        
        
        
        }        
                
        } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }
  */      
        } else {
            return false;
        }
        return true;
    }
    public int countUsersGuild(Statement stmt, int GuildID){
            int n = 0;
        try {
            ResultSet UserCount = stmt.executeQuery("SELECT * FROM Users WHERE GuildID = "+GuildID);

            while(UserCount.next()){
                getLogger().info("User found in Guild: " + UserCount.getString("UUID"));
                n++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
            return n;
    }
    public void echo(String Echo){
        getLogger().info(Echo);
    }
    public void echo(Player player, String Echo){
        player.sendMessage(Echo);
    }
    
    
    
    
    public void PlayerChatEvent(Player player, String message){
        try {
            int InGuild_id = 9999;
            
            ResultSet isinGuild = stmt.executeQuery("SELECT * FROM Users WHERE uuid'"+player.getUniqueId().toString()+"'");
            if(isinGuild.next()){
                InGuild_id = isinGuild.getInt("guildID");
            
            }
            
            for(Player p : Bukkit.getOnlinePlayers()){
                if(ContainsPlayer(p, InGuild_id)){
                    p.sendMessage(ChatColor.GREEN + "guildchat: " +message);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public boolean ContainsPlayer(Player p, int guildid) {

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE '" + p.getUniqueId().toString() + "'");

            while (!rs.next()) {
                String teller = rs.getString("uuid");
                if (teller == p.getUniqueId().toString()) {
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public void ListGuilds(Player player) throws SQLException{
        ResultSet Guilds = stmt.executeQuery("SELECT * FROM Guilds");
        while(Guilds.next()){
            player.sendMessage("ID: " + Guilds.getString("GuildID") + " | Guild: " + Guilds.getString("Name"));
            
        }
        
    }
    public void PrintGuildInfo(Player player){
        try {
                List<String> Messages = new ArrayList<String>();
                
                    //Find out if user is in a guild
                    ResultSet GuildIDCatch = stmt.executeQuery("SELECT * FROM Users WHERE UUID = '"+player.getUniqueId().toString()+"'");
                    if(!GuildIDCatch.next()){
                        Messages.add("You are not in a Guild");
                    } else {
                        //User is in guild, find out guild information
                        int GuildID = GuildIDCatch.getInt("GuildID");
                        ResultSet GuildInfo = stmt.executeQuery("SELECT * FROM Guilds WHERE GuildID = "+GuildID);
                        if(GuildInfo.next()){
                            //player.sendMessage("GuildID = >>"+GuildID+"<<");

                            
                            Messages.add(ChatColor.GREEN + "Your Guild Overview");
                            Messages.add(ChatColor.MAGIC + "Guild: "+ ChatColor.GREEN + "<<<" + GuildInfo.getString("GuildName") + ">>>");
                            Messages.add(ChatColor.GRAY + "| Created at: "+GuildInfo.getString("created"));
                            Messages.add(ChatColor.GREEN + "Motd: " + GuildInfo.getString("Motd"));
                            
                            ResultSet PlayerInfo = stmt.executeQuery("SELECT * FROM Users WHERE UUID ='"+GuildInfo.getString("Owner")+"'");
                            PlayerInfo.next();
                            
                            
                            Messages.add("######Users#######");
                            

                            ResultSet PlayersinGuild = stmt.executeQuery("SELECT * FROM Users INNER JOIN Ranks ON Users.GuildRank = Ranks.RankID WHERE GuildID = "+GuildID+" ORDER BY Users.GuildRank DESC");
                            //Get All the Players from a Guild
                            int Playercount = 0;
                            while(PlayersinGuild.next()){
                                Playercount++;
                                Messages.add(ChatColor.DARK_GREEN+"|<"+PlayersinGuild.getString("Name")+">| : |<"+PlayersinGuild.getString("RankName")+">|");
                            }
                            
                        } else {
                            Messages.add("Couldn retrive information");
                        }
                    }
                    sendMessages(Messages, player);
                
                } catch (SQLException ex) {
                    Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    public void sendMessages(List<String> Messages, Player player){
                    for(String m : Messages){
                        player.sendMessage(m);
                    }
    }
}

class Guild{
    int GuildID;
    public void setGuildID(int guildID){
        GuildID = guildID;
    }
    String GuildName;
    public void setGuildName(String guildname){
        GuildName = guildname;
    }
    HashMap<Integer,Player> GuildPlayers = new HashMap<Integer,Player>();
    public void addUser(Player player){
        
    }
    public void printdata(Player player){
        player.sendMessage("GuildName: "+GuildName + " with ID = "+GuildID);
    }
    
}
