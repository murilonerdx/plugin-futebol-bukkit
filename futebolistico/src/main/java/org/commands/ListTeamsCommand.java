package org.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.Futebolistico.*;

public class ListTeamsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if(!teams.isEmpty()){
                teams.forEach(
                        team -> sender.sendMessage(team.getColor().toString() + "" + team.getName() + " <"+team.getPlayersGoals().size()+"> players " + "\n")
                );
                return true;
            }else{
                sender.sendMessage(ChatColor.RED + "Nenhum time cadastrado");
                return true;
            }
        }
        return false;
    }

}