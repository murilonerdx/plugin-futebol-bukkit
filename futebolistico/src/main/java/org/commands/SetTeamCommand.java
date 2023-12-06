package org.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.utils.objects.Position;
import org.utils.objects.PositionCampEnum;
import org.utils.objects.Team;

import java.util.Optional;

import static org.Futebolistico.*;
import static org.utils.PlayerUtils.setPlayerInTeamNoCreate;
import static org.utils.SideBarUils.setupSidebar;

public class SetTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 3) {
            String nameTeam = args[1];
            Player player = Bukkit.getPlayer(args[0]);

            Optional<Team> matchingTeam = teams.stream()
                    .filter(t -> t.getName().equals(nameTeam))
                    .findFirst();

            PositionCampEnum positionCampEnum = PositionCampEnum.valueOf(args[2]);

            if (matchingTeam.isPresent() && player != null && positionCampEnum != null) {
                Team team = matchingTeam.get();
                long countTeam = team.getPlayersGoals().size();

                if(teams.stream().noneMatch(t -> t.getPlayersGoals().containsKey(player))){
                    if (countTeam < 3) {
                        setPlayerInTeamNoCreate(nameTeam, player, new Position(positionCampEnum, 0L));
                        setupSidebar(nameTeam, 0);
                        player.sendMessage("Você foi adicionado com sucesso ao time");
                        return true;
                    } else {
                        player.sendMessage("Esse time já está cheio, veja outros times /teams");
                        return false;
                    }
                } else {
                    player.sendMessage("Você já está em um time cadastrado!");
                    return false;
                }
            } else {
                sender.sendMessage("Time não encontrado ou player");
                return false;
            }
        }
        return false;
    }

}