package org.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.Futebolistico.*;
import static org.utils.PlayerUtils.getPlayerTeam;
import static org.utils.PlayerUtils.playerHaveTeam;
import static org.utils.SideBarUils.updateSidebar;

public class ExitTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (playerHaveTeam(player) && getPlayerTeam(player).isPresent()) {

                teams.stream()
                        .filter(t -> t.getName().equals(getPlayerTeam(player).get()))
                        .findFirst()
                        .ifPresent(team -> {
                            team.getPlayersGoals().remove(player);
                            updateSidebar();
                        });

                updateSidebar();
                return true;
            } else {
                player.sendMessage("Você não pertence a nenhum time no momento, para ver os times existentes digite: /times");
            }
        }
        return false;
    }

}