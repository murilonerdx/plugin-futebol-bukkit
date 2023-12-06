package org.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.objects.Team;

import java.util.Optional;

import static org.Futebolistico.*;

public class TeamGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2) {
            String teamA = args[0];
            String teamB = args[1];

            if (teamA != null && teamB != null) {
                Optional<Team> matchingTeam = teams.stream()
                        .filter(t -> t.getName().equals(teamA))
                        .findFirst();

                Optional<Team> matchingTeam2 = teams.stream()
                        .filter(t -> t.getName().equals(teamB))
                        .findFirst();

                if (matchingTeam2.isPresent() && matchingTeam.isPresent() && !game.getGameLock()) {
                    game.setTeam1(matchingTeam.get());
                    game.setTeam2(matchingTeam2.get());

                    matchingTeam2.get().getPlayersGoals().keySet().forEach(t -> t.sendMessage("Seu time " + matchingTeam2.get().getColor() + matchingTeam2.get().getName() + ChatColor.WHITE + " foi escolhido para o proximo jogo"));
                    matchingTeam.get().getPlayersGoals().keySet().forEach(t -> t.sendMessage("Seu time " + matchingTeam2.get().getColor() + matchingTeam.get().getName() + ChatColor.WHITE +" foi escolhido para o proximo jogo"));
                }


            } else {
                sender.sendMessage("Há times que não existem!");
            }
        }

        return true;
    }
}