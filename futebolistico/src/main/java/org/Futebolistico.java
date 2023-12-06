package org;

import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.utils.initialize.InitializePlugin;
import org.utils.objects.Game;
import org.utils.objects.PunishPlayer;

import java.util.*;

import static org.utils.SideBarUils.sideBar;

public class Futebolistico extends JavaPlugin implements Listener {
    public static Futebolistico instance;
    public static Slime ball = null;

    public static Game game = new Game();

    public static List<org.utils.objects.Team> teams = new ArrayList<>();
    public static ScoreboardManager manager;
    public static Scoreboard board;
    public static Objective objective;
    public static Player lastPlayerTouchedBall;
    public static List<PunishPlayer> punishPlayers = new ArrayList<>();
    public InitializePlugin<Futebolistico> initializePlugin;
    public static Map<UUID, Integer> lavaTraps = new HashMap<>();
    @Override
    public void onEnable() {
        instance = this;
        initializePlugin = new InitializePlugin<>(this);
        sideBar("Placar futebol", "PlacarFutebol");
        initializePlugin.registerEvents();
        initializePlugin.schedulesThreadsView();
        initializePlugin.executeCommands();
        initializePlugin.registerServerEvents();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void addPunishPlayer(PunishPlayer punishPlayer){
        punishPlayers.add(punishPlayer);
    }

    public Objective getObjective() {
        return objective;
    }
}
