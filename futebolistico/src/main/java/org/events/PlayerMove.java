package org.events;

import org.Futebolistico;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collection;


public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Futebolistico.ball == null) return;

        Collection<Entity> nearbyEntities = e.getPlayer().getWorld().getNearbyEntities(e.getPlayer().getLocation(), 0.8, 0.8, 0.8);
        if (!nearbyEntities.contains(Futebolistico.ball)) return;

        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5F, 1f);

        if (e.getPlayer().isSneaking()) {
            Futebolistico.ball.setVelocity(Futebolistico.ball.getVelocity().setY(0.6));
            return;
        }

        double kickPower = 1.5;
        if (e.getPlayer().isSprinting()) kickPower = kickPower * 1.4;
        Futebolistico.ball.setVelocity(e.getPlayer().getLocation().getDirection().multiply(kickPower).setY(0));



    }
}
