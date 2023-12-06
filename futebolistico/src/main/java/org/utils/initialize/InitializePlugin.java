package org.utils.initialize;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.commands.*;
import org.events.EntityDamage;
import org.events.PlayerMove;
import org.listener.BallInteractionListener;
import org.listener.CursedItemListener;
import org.listener.PlayerEventServerListener;
import org.utils.CheckGoal;

import java.util.Objects;

public class InitializePlugin<T extends JavaPlugin> {
    private final T principal;

    public InitializePlugin(T principal) {
        this.principal = principal;
    }

    public void executeCommands() {
        Objects.requireNonNull(principal.getCommand("futebol")).setExecutor(new FutebolCommand());
        Objects.requireNonNull(principal.getCommand("create-team")).setExecutor(new TeamCommand());
        Objects.requireNonNull(principal.getCommand("setteam")).setExecutor(new SetTeamCommand());
        Objects.requireNonNull(principal.getCommand("startgame")).setExecutor(new StartGameCommand());
        Objects.requireNonNull(principal.getCommand("finishgame")).setExecutor(new FinishGameCommand());
        Objects.requireNonNull(principal.getCommand("exit-team")).setExecutor(new ExitTeamCommand());
        Objects.requireNonNull(principal.getCommand("times")).setExecutor(new ListTeamsCommand());
        Objects.requireNonNull(principal.getCommand("team-game")).setExecutor(new TeamGameCommand());
        Objects.requireNonNull(principal.getCommand("punish")).setExecutor(new PunishCommand());
    }

    public void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), principal);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), principal);
    }

    public void registerServerEvents(){
        principal.getServer().getPluginManager().registerEvents(new BallInteractionListener(), principal);
        principal.getServer().getPluginManager().registerEvents(new PlayerEventServerListener<>(principal), principal);
        principal.getServer().getPluginManager().registerEvents(new CursedItemListener(), principal);
    }

    public void schedulesThreadsView(){
        BukkitScheduler scheduler = principal.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(principal, CheckGoal::checkGoal, 0L, 2L);
    }
}
