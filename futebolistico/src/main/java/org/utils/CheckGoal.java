package org.utils;

import org.Futebolistico;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.utils.objects.LocationPosition;
import org.utils.objects.PositionCampGetLocation;
import org.utils.objects.Team;

import java.util.List;
import java.util.Optional;

import static org.Futebolistico.*;
import static org.Futebolistico.board;
import static org.utils.PlayerUtils.*;
import static org.utils.Spawn.spawnBall;

public class CheckGoal {
    static Futebolistico instance = Futebolistico.instance;

    public static void checkGoal() {
        if (Futebolistico.ball == null) return; // Add this line to prevent NullPointerException

        List<BoundingBox> goals = (List<BoundingBox>) instance.getConfig().getList("goals");
        String world = instance.getConfig().getString("world");
        if (world == null) return;
        World world1 = Bukkit.getWorld(world);

        Location ballLoc = Futebolistico.ball.getLocation();
        assert goals != null;
        for (BoundingBox goal : goals) {
            if (!goal.contains(ballLoc.getX(), ballLoc.getY(), ballLoc.getZ())) continue;
            Location fireworkLoc = goal.getCenter().toLocation(world1);
            spawnFireworks(fireworkLoc, Color.LIME);
            spawnFireworks(fireworkLoc, Color.YELLOW);
            Player nearestPlayer = findNearestPlayer(ballLoc, world1);
            if (nearestPlayer != null) {
                goal(nearestPlayer);
            }

            spawnBall();
        }
    }

    private static Player findNearestPlayer(Location location, World world) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;
        long v = 5, v1 = 5, v2 = 5;

        if (lastPlayerTouchedBall != null) {
            Bukkit.getPlayer(lastPlayerTouchedBall.getName());
        } else if (!world.getNearbyEntities(location, v, v1, v2).isEmpty()) {
            nearestPlayer = getPlayer(location, world, nearestPlayer, nearestDistanceSquared, v, v1, v2);
        } else {
            v = 20;
            v1 = 20;
            v2 = 20;
            nearestPlayer = getPlayer(location, world, nearestPlayer, nearestDistanceSquared, v, v1, v2);
        }

        if (nearestPlayer != null && playerHaveTeam(nearestPlayer) && getPlayerTeam(nearestPlayer).isPresent()) {
            String team = getPlayerTeam(nearestPlayer).get();
            Objective objective = Futebolistico.board.getObjective(DisplaySlot.SIDEBAR);
            if (objective != null) {
                Score score = objective.getScore(team);
                score.setScore(score.getScore() + 1);
            } else {
                // Handle the case where the objective is null
                // For example, log an error or create the objective
            }
        }

        return nearestPlayer;
    }

    private static Player getPlayer(Location location, World world, Player nearestPlayer, double nearestDistanceSquared, long v, long v1, long v2) {
        for (Entity entity : world.getNearbyEntities(location, v, v1, v2)) {
            if (entity instanceof Player) {
                double distanceSquared = entity.getLocation().distanceSquared(location);
                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared;
                    nearestPlayer = (Player) entity;
                }
            }
        }
        return nearestPlayer;
    }

    public static void updateScore(String teamName, Long score) {
        Score scoreEntry = objective.getScore(teamName);
        scoreEntry.setScore(score.intValue());
    }

    public Scoreboard getBoard() {
        return board;
    }

    public static void goal(Player player) {
        // Existing code to update the score

        player.sendTitle(ChatColor.GOLD + "Gol!", ChatColor.GREEN + "Você fez 1 gol", 10, 70, 20);
        // Atualiza o placar do jogador
        Score playerScore = Futebolistico.instance.getObjective().getScore(player.getName());
        playerScore.setScore(playerScore.getScore() + 1);

        // Atualiza o placar do time
        Optional<String> teamNameOpt = getPlayerTeam(player);
        if (teamNameOpt.isPresent()) {
            String teamName = teamNameOpt.get();
            Team team = getTeamByName(teamName);
            if (team != null) {
                team.addGoal(player);
                updateScore(team.getName(), team.getTotalGoals());
            } else {
                updateScore(player.getName(), (long) playerScore.getScore());
            }
        } else {
            updateScore(player.getName(), (long) playerScore.getScore());
        }

        // Teleport all players to a specific location and freeze them
        LocationPosition atacanteTa = PositionCampGetLocation.ATACANTE_TA.getPosition();
        Location spawnLocation = new Location(Bukkit.getWorld("world"), atacanteTa.getX(), atacanteTa.getY(), atacanteTa.getZ()); // Adjust coordinates as needed
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.teleport(spawnLocation);
            onlinePlayer.setWalkSpeed(0); // Freeze players
        }

        // Schedule a task to unfreeze players after 3 seconds and play a sound
        Bukkit.getScheduler().scheduleSyncDelayedTask(Futebolistico.instance, () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.setWalkSpeed(0.2f); // Unfreeze players (default walk speed)
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f); // Play sound
            }
        }, 60L); // 60L represents 3 seconds (20 ticks = 1 second)
    }

    static void spawnFireworks(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(
                FireworkEffect
                        .builder()
                        .withColor(color)
                        .flicker(true)
                        .build()
        );

        firework.setFireworkMeta(fireworkMeta);
    }
}
