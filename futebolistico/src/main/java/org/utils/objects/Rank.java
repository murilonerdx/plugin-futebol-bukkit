package org.utils.objects;

import org.bukkit.ChatColor;

public enum Rank {
    ADMIN(ChatColor.GOLD + "ADMIN "),
    MODERATOR(ChatColor.DARK_PURPLE + "MODERATOR "),
    DEV(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "DEV "),
    VIP(ChatColor.YELLOW + "VIP "),
    VIEWER(ChatColor.AQUA + "VIEWER "),
    PLAYER(ChatColor.DARK_BLUE + "PLAYER");

    private String display;

    Rank(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
