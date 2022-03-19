package zorg.bchru.Interfaces;

import main.GuildCore;
import main.utils.console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class quarry {


    public String host, port, database, username, password;
    //static MysqlDataSource data = new MysqlDataSource();
    static Statement stmt;
    static Connection conn;
    public static FileConfiguration config;
    static String prefix = "GuildCore_";


    public boolean init() throws SQLException {

        Bukkit.getConsoleSender().sendMessage("Setting up Mysql Connection");

        String Address = "localhost";
        String Database = "mc_Autumn";
        String DatabaseUser = "Autumn";
        String DatabaseUserPassword = "Hihihi45";
//        conn = DriverManager.getConnection("jdbc:mysql://"+config.getString("Address")+"/"+config.getString("Database"),config.getString("DatabaseUser"),config.getString("Password"));
        conn = DriverManager.getConnection("jdbc:mysql://" + Address + "/" + Database, DatabaseUser, DatabaseUserPassword);

        stmt = (Statement) conn.createStatement();
        console.echo("Database Connection Established; Testing querry:");
        return true;
    }

    public boolean initTables() throws SQLException {
        try {console.echo("Checking & Creating Tables");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"Boop(BoopID int PRIMARY KEY NOT NULL AUTO_INCREMENT, PlayerID varchar(255), TargetID varchar(255), Time timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"JoinMessages(ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, PlayerID varchar(255), Message TEXT, Time timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"Guilds (GuildID int PRIMARY KEY NOT NULL AUTO_INCREMENT, GuildName varchar(255), Owner varchar(255),Motd TEXT, created timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"Users (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, UUID varchar(255), GuildID int, GuildRank int, Name varchar(255), invitedTime timestamp DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"Invites (ID int PRIMARY KEY NOT NULL AUTO_INCREMENT, Inviter varchar(255), Target varchar(255), GuildID int, ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS "+prefix+"Ranks (RankID int PRIMARY KEY NOT NULL, RankName varchar(255))");
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    public boolean droptables() throws SQLException {
        try {
            console.echo("Dropping all Tables");
            stmt.execute("DROP TABLE "+prefix+"Boop, "+prefix+"JoinMessages, "+prefix+"Guilds, "+prefix+"Users, "+prefix+"Invites, "+prefix+"Ranks");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GuildCore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public String getGuildIDfromPlayer(Player p) throws SQLException{
        Player P = p;
        ResultSet GuildsData = stmt.executeQuery("SELECT * from "+prefix+"Users where UUID='"+p.getUniqueId().toString()+"'");
        String GuildID = "";
        if(GuildsData.next()){
            GuildID = GuildsData.getString("GuildID");
        } else {
            GuildID = "-1";
        }
        return GuildID;
    }


    public String getGuildIDfromPlayerName(String s) throws SQLException{

        ResultSet GuildsData = stmt.executeQuery("SELECT * from "+prefix+"Users where Name='"+s+"'");
        String GuildID = "";
        if(GuildsData.next()){
            GuildID = GuildsData.getString("GuildID");
        } else {
            GuildID = "-1";
        }
        return GuildID;
    }
    public boolean sendMessagetoGuild(Player p, String s) throws SQLException {
        String GuildID = getGuildIDfromPlayer(p);
        if(GuildID == "-1"){
            p.sendMessage("You need to join a guild to use guild chat");
        }
        ResultSet PlayerListofGuild = stmt.executeQuery("Select * from "+prefix+"Users Where GuildID='"+GuildID+"'");

        while(PlayerListofGuild.next()) {
            for(Player op :  Bukkit.getOnlinePlayers()){
                if(op.getUniqueId().toString() == PlayerListofGuild.getString("UUID")){
                    op.sendMessage(ChatColor.GREEN + "Guild >: "+p.getName().toString().toUpperCase()+ " :< " +ChatColor.YELLOW + " :>" + s);

                }
            }
        }
        return true;
    }

}
