package org.utils;

import org.Futebolistico;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.scoreboard.Score;
import org.utils.objects.Team;


public class GameTimer extends BukkitRunnable {
    private int timeLeft; // Tempo em segundos
    private final Team time1;
    private final Team time2;
    private BossBar bossBar;
    private final int minutes;
    private boolean gameFinish = false;

    public GameTimer(int minutes, Team time1, Team time2) {
        Score scoreTeamA = Futebolistico.instance.getObjective().getScore(time1.getName());
        Score scoreTeamB = Futebolistico.instance.getObjective().getScore(time2.getName());

        this.minutes = minutes;
        this.bossBar = Bukkit
                .createBossBar(
                        time1.getColor() + time1.getName() + " " + scoreTeamA.getScore() + ChatColor.WHITE + " X " + time2.getColor() + " " + scoreTeamB.getScore() + " " + time2.getName(), BarColor.BLUE, BarStyle.SOLID);
        this.bossBar.setVisible(true);
        this.bossBar.setStyle(BarStyle.SOLID);

        this.time1 = time1;
        this.time2 = time2;
        this.timeLeft = minutes * 60;
    }

    @Override
    public void run() {
        if (gameFinish) {
            return;
        }

        Score scoreTeamA = Futebolistico.instance.getObjective().getScore(time1.getName());
        Score scoreTeamB = Futebolistico.instance.getObjective().getScore(time2.getName());

        if (timeLeft <= 0) {
            // Lógica para quando o tempo acabar
            Bukkit.broadcastMessage("O jogo acabou!");
            bossBar.removeAll();

            time1.getPlayersGoals().keySet().forEach(t -> t.performCommand("finishgame"));
            time2.getPlayersGoals().keySet().forEach(t -> t.performCommand("finishgame"));
            gameFinish = true;
            this.cancel();
            try {
                this.finalize();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String timeFormatted = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Tempo restante: " + timeFormatted));
        }

        bossBar.setTitle(
                time1.getColor() + time1.getName() + " " + scoreTeamA.getScore() + ChatColor.WHITE + " X " + time2.getColor() + scoreTeamB.getScore() + " " + time2.getName());
        bossBar.setProgress((double) timeLeft / (this.minutes * 60));

        // Add all online players to the BossBar
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
        timeLeft--;
    }
}