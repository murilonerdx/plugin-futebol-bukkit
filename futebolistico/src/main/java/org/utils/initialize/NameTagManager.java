package org.utils.initialize;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.utils.objects.Rank;

import java.util.Objects;

public class NameTagManager {

    public static void setNametags(Player player){
        player.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard());

        for(Rank rank : Rank.values()){
            Team team = player.getScoreboard().registerNewTeam(rank.name());
            team.setPrefix(ChatColor.translateAlternateColorCodes('&',rank.getDisplay()));
        }
    }

    public static void newTag(Player player){
        if(player.getName().equals("the_developer_")){
            Rank rank = Rank.DEV;
            for(Player target : Bukkit.getOnlinePlayers()){
                Objects.requireNonNull(target.getScoreboard().getTeam(rank.name())).addEntry(player.getName());
            }
        }else{
            Rank rank = Rank.PLAYER;
            for(Player target : Bukkit.getOnlinePlayers()){
                Objects.requireNonNull(target.getScoreboard().getTeam(rank.name())).addEntry(player.getName());
            }
        }
    }

    public static void remove(Player player){
        for(Player target : Bukkit.getOnlinePlayers()){
            Objects.requireNonNull(target.getScoreboard().getEntryTeam(player.getName())).removeEntry(player.getName());
        }
    }
}
