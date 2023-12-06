package org.listener;

import org.Futebolistico;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BallInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteractWithBall(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().equals(Futebolistico.ball)) {
            Futebolistico.lastPlayerTouchedBall = event.getPlayer();
        }
    }
}
