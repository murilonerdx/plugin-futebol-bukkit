package org.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.utils.objects.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.Futebolistico.*;
import static org.utils.PlayerUtils.*;
import static org.utils.SideBarUils.setupSidebar;
import static org.utils.SideBarUils.updateSidebar;

public class LoadConfig<T extends JavaPlugin> implements Listener {
    private final T principal;

    public LoadConfig(T principal) {
        this.principal = principal;
    }

    public void savePlayerData(Player player) {
        FileConfiguration config = principal.getConfig();
        Optional<String> teamName = getPlayerTeam(player);
        String pathBase = "players." + player.getUniqueId() + ".";
//
//        Optional<PunishPlayer> isBanish = punishPlayers.stream().filter(t -> t.getPlayer().equals(player.getUniqueId())).findFirst();
//        if (isBanish.isPresent()) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            String formattedDate = formatter.format(isBanish.get().getDate());
//            config.set(pathBase + "ban.temp", formattedDate);
//            config.set(pathBase + "ban.ispunish", isBanish.get().isPunish());
//            config.set(pathBase + "ban.type", isBanish.get().getTypeBan());
//            config.set(pathBase + "ban.inventory", isBanish.get().getInventory());
//        }

        if (teamName.isPresent()) {

            config.set(pathBase + "team", teamName.get());

            config.set("global." + "teams", teams.toString());
            Position position = getPlayerPosition(player);
            if (position != null) {
                config.set(pathBase + "position", position.getNamePosition().name());
                config.set(pathBase + "gols", position.getQtdGoals());
            }
        }
        principal.saveConfig();
    }

    public void loadPlayerData(Player player) throws ParseException {
        FileConfiguration config = principal.getConfig();
        String pathBase = "players." + player.getUniqueId() + ".";
        String teamName = config.getString(pathBase + "team");
        String positionName = config.getString(pathBase + "position");
        Long gols = config.getLong(pathBase + "gols");
        boolean ban = config.getBoolean(pathBase + "ban");
        String banTemp = config.getString(pathBase + "ban.temp");
        String typeBan = config.getString(pathBase + "ban.type");
        List<?> punishPlayer = config.getList(pathBase + "ban.inventory", Collections.singletonList(InventorySave.class));

//        if (!ban && typeBan != null && banTemp != null) {
//            type(typeBan, banTemp, player, punishPlayer);
//        }

        if (teamName != null && positionName != null) {
            Position position = new Position(PositionCampEnum.valueOf(positionName), gols);
            Team team = getTeamByName(teamName);
            if (team != null) {
                Optional<Player> any = team.getPlayersGoals().keySet().stream().filter(v -> v.getUniqueId().equals(player.getUniqueId())).findAny();
                if (any.isPresent()) {
                    teams.forEach(t -> t.removePlayer(any.get()));
                    team.getPlayersGoals().put(player, position);
                    updateSidebar();
                    setupSidebar(team.getName(), gols.intValue());
                }
            }
        }

        principal.saveConfig();
    }

//
//    public void type(String command, String banTemp, Player player, List<?> punishPlayer) throws ParseException {
//        if (command.equalsIgnoreCase("punish_inventory_temp")) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            boolean before = formatter.parse(banTemp).before(new Date());
//            if (!before) {
//                createChestWithItems(Objects.requireNonNull(player.getPlayer()).getUniqueId(), (List<InventorySave>) punishPlayer);
//            }
//        }
//    }
}
