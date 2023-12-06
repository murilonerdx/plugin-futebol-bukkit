package org.utils.objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Team {
    private String teamId;
    private String name;
    private ChatColor color;
    private HashMap<Player, Position> playersGoals = new HashMap<>();
    private Long totalGoals = 0L;

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public void addGoal(Player player) {
        // Adiciona um gol ao jogador específico
        Position position = playersGoals.get(player);
        position.setQtdGoals(position.getQtdGoals() + 1);
        playersGoals.put(player, position);
        // Atualiza o total de gols do time
        totalGoals = totalGoals + 1;
    }

    public Long getTotalGoals() {
        return totalGoals;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Team(String name, ChatColor color, HashMap<Player, Position> playersGoals) {
        this.name = name;
        this.color = color;
        this.playersGoals = playersGoals;
    }

    public HashMap<Player, Position> getPlayersGoals() {
        return playersGoals;
    }

    public Team removePlayer(Player player){
        this.getPlayersGoals().remove(player);

        return this;
    }

    public void setPlayersGoals(HashMap<Player, Position> playersGoals) {
        this.playersGoals = playersGoals;
    }

    public void setTotalGoals(Long totalGoals) {
        this.totalGoals = totalGoals;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId='" + teamId + '\'' +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", playersGoals=" + playersGoals +
                ", totalGoals=" + totalGoals +
                '}';
    }
}
