package org.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.utils.LoadConfig;
import org.utils.initialize.NameTagManager;

import java.text.ParseException;



public class PlayerEventServerListener<T extends JavaPlugin> implements Listener {

    private final LoadConfig<T> loadConfig;

    public PlayerEventServerListener(T principal){
        this.loadConfig = new LoadConfig<>(principal);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws ParseException {
        NameTagManager.setNametags(event.getPlayer());
        NameTagManager.newTag(event.getPlayer());
        loadConfig.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Limpa os itens dropados
        event.getDrops().clear();

        // Se você também quiser limpar o inventário do jogador (opcional)
        event.getEntity().getInventory().clear();
    }
}

