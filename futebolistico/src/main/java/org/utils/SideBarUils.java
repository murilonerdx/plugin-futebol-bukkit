package org.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.utils.objects.Position;
import org.utils.objects.PositionCampEnum;

import java.util.Map;

import static org.Futebolistico.*;

public class SideBarUils {

    public static void updateSidebar() {
        board.clearSlot(DisplaySlot.SIDEBAR);
        for (org.utils.objects.Team team : teams) {
            String teamInfo = team.getColor() + team.getName() + " - " + team.getTotalGoals() + " gols";
            Score teamScore = objective.getScore(teamInfo);
            teamScore.setScore(Math.toIntExact(team.getTotalGoals()));
        }

        for (org.utils.objects.Team team : teams) {
            for (Map.Entry<Player, Position> entry : team.getPlayersGoals().entrySet()) {
                Player player = entry.getKey();
                Position position = entry.getValue();
                Score playerScore = objective.getScore(player.getName());
                playerScore.setScore(Math.toIntExact(position.getQtdGoals()));
            }
        }
    }

    public static void updateScore(String teamName, int newScore) {
        Score score = objective.getScore(teamName);
        score.setScore(newScore);
    }

    public static void sideBar(String title, String id) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        objective = board.registerNewObjective(id, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }


    public static void setupSidebar(String title, int scorePoint) {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();

        // Define o título e o critério do placar
        objective = board.registerNewObjective("PlacarFutebol", "dummy", ChatColor.GOLD + "Placar Futebol");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Exemplo de como adicionar linhas ao placar
        Score score = objective.getScore(title);
        score.setScore(scorePoint);

        // Define o placar para todos os jogadores online
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(board);
        }
    }
}
