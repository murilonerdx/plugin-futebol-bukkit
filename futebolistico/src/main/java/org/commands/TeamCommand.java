package org.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.objects.Position;
import org.utils.objects.PositionCampEnum;
import org.utils.objects.Team;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static org.Futebolistico.*;
import static org.utils.PlayerUtils.getPlayerTeam;
import static org.utils.PlayerUtils.playerHaveTeam;
import static org.utils.SideBarUils.setupSidebar;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 3) {

            String team = args[1];
            Player player = Bukkit.getPlayer(args[0]);
            ChatColor color = ChatColor.valueOf(args[2].toUpperCase());

            if (teams.stream().anyMatch(t -> t.getColor() == color)) {
                sender.sendMessage("Por favor digite outra cor, essa já está sendo usada por uma equipe " + color);
            } else {
                if (player != null && playerHaveTeam(player)) {
                    player.sendMessage("Você já tem um time, por favor saia para entrar em outro /exit-team");
                    return true;
                }
                if (sender != null && color != null && teams.stream().noneMatch(t -> Objects.equals(t.getName(), team))) {
                    Optional<String> playerTeam = getPlayerTeam(player);
                    if (playerTeam.isPresent()) {
                        String playerTeamName = playerTeam.get();
                        teams.stream().filter(t -> t.getName().equals(playerTeamName))
                                .forEach(t -> t.getPlayersGoals().remove(player));

                        if(player != null){
                            player.sendMessage("Você saiu de todos os times para criar esse");
                        }
                    }

                    if(player != null){
                        HashMap<Player, Position> positions = new HashMap<>();
                        positions.put(player, new Position(PositionCampEnum.TECNICO, 0L));
                        teams.add(new Team(team, color, positions));
                        setupSidebar(player.getName(), 0);
                        setupSidebar(team, 0);
                        sender.sendMessage(color + "Jogador " + player.getName() + " atribuído ao " + team);
                        return true;
                    }
                }
            }
        }
        return false;
    }


}