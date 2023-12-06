package org.utils;

import org.Futebolistico;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;


public class Spawn {
    static Futebolistico plugin = Futebolistico.instance;

    public static void spawnBall() {
        Location spawn = (Location) plugin.getConfig().get("spawn");
        if (Futebolistico.ball != null) Futebolistico.ball.remove();
        Futebolistico.ball = (Slime) spawn.getWorld().spawnEntity(spawn, EntityType.SLIME);
        Futebolistico.ball.setSize(1);
        Futebolistico.ball.setAware(false);
    }
}
