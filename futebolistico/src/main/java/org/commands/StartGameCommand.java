package org.commands;

import org.Futebolistico;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.GameTimer;
import org.utils.objects.Team;


import static org.Futebolistico.*;

public class StartGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1) {
            if (game.verifyTeamsOpenInGame()) { // Ensure there is at least one element in the list
                Team time1 = game.getTeam1(); // This line should now be safe
                Team time2 = game.getTeam2();

                game.setGameLock(true);
                new GameTimer(Integer.parseInt(args[0]), time1, time2).runTaskTimer(instance, 1, 20);
            } else {
                sender.sendMessage("Os times não foram cadastrados em team-game");
                return true;
            }
        }else{
            sender.sendMessage("Defina a quantidade de tempo que quer que o jogo dure");
            return true;
        }

        return true;
    }


}