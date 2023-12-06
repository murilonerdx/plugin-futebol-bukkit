package org.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.Futebolistico.*;
import static org.utils.PlayerUtils.getPlayerTeam;
import static org.utils.PlayerUtils.playerHaveTeam;
import static org.utils.CheckGoal.updateScore;
import static org.utils.Spawn.spawnBall;

public class FutebolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            resetScoreboard();
            spawnBall();
            if(playerHaveTeam((Player) commandSender) && getPlayerTeam((Player) commandSender).isPresent()){
                updateScore(getPlayerTeam((Player) commandSender).get(), 0L);
            }
            return true;
        }
        return false;
    }

    public void resetScoreboard() {
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }
    }


}
