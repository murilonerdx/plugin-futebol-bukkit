package org.commands;

import org.Futebolistico;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.utils.objects.Team;

import java.awt.*;
import java.util.*;

import static org.Futebolistico.*;

public class FinishGameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(game.getTeam1() != null && game.getTeam2() != null){
            Team timeVencedor;
            Team timePerdedor;
            boolean empate = false;
            Long quantidadeGoalsVencedor;

            Team team1 = game.getTeam1();
            Team team2 = game.getTeam2();

            if (team1.getTotalGoals() > team2.getTotalGoals()) {
                timeVencedor = team1;
                timePerdedor = team2;
                quantidadeGoalsVencedor = team1.getTotalGoals();
                result(timeVencedor, timePerdedor, quantidadeGoalsVencedor);
            }else if(Objects.equals(team1.getTotalGoals(), team2.getTotalGoals())) {
                empate = true;
            }else{
                timeVencedor = team2;
                timePerdedor = team1;
                quantidadeGoalsVencedor = team2.getTotalGoals();
                result(timeVencedor, timePerdedor, quantidadeGoalsVencedor);
            }

            if(empate){
                String templateFinal = "Houve um empate";

                team1.getPlayersGoals()
                        .keySet()
                        .forEach(p -> p.sendMessage(ChatColor.YELLOW + "" + templateFinal));

                team2
                        .getPlayersGoals()
                        .keySet()
                        .forEach(p -> p.sendMessage(ChatColor.RED + "" + templateFinal));
            }

            board.resetScores(team1.getName());
            board.resetScores(team2.getName());
            objective.getScore(team1.getName()).setScore(0);
            objective.getScore(team2.getName()).setScore(0);
            teams.forEach(t -> t.setTotalGoals(0L));
            game.setTeam1(null);
            game.setTeam2(null);
            game.setGameLock(false);

            return true;
        }else{
            sender.sendMessage("Time não está configurado");
            return true;
        }
    }

    private static void result(Team timeVencedor, Team timePerdedor, Long quantidadeGoalsVencedor) {
        String templateFinal = "Time vencedor é %s (%s)";
        StringBuilder resultadoFinal;
        resultadoFinal = new StringBuilder(String.format(templateFinal, timeVencedor.getName(), quantidadeGoalsVencedor.intValue()));

        timeVencedor
                .getPlayersGoals()
                .keySet()
                .forEach(p -> p.sendMessage(ChatColor.YELLOW + "Seu time ganhou parabéns!! " + resultadoFinal));

        timePerdedor
                .getPlayersGoals()
                .keySet()
                .forEach(p -> p.sendMessage(ChatColor.RED + "Infelizmente não foi dessa vez!!" + resultadoFinal));
    }

}