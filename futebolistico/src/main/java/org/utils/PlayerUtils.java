package org.utils;

import org.Futebolistico;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.utils.objects.*;

import java.util.*;

import static org.Futebolistico.*;

public class PlayerUtils {
    public static Team getTeamByName(String teamName) {
        for (org.utils.objects.Team team : teams) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }
        return null;
    }

    public static Position getPlayerPosition(Player player) {
        if (playerHaveTeam(player)) {
            Optional<String> playerTeam = getPlayerTeam(player);
            if (playerTeam.isPresent()) {
                Team teamByName = getTeamByName(playerTeam.get());

                if (teamByName != null) {
                    return teamByName.getPlayersGoals().get(player);
                }

            }
        }
        return null;
    }

    public static Optional<String> getPlayerTeam(Player player) {
        return teams
                .stream()
                .filter(team -> team.getPlayersGoals().get(player) != null)
                .map(org.utils.objects.Team::getName)
                .findAny();
    }


    public static void setPlayerInTeamNoCreate(String team, Player player, Position position) {
        Optional<org.utils.objects.Team> matchingTeam = teams.stream()
                .filter(t -> t.getName().equals(team))
                .findFirst();

        if (matchingTeam.isPresent() && !matchingTeam.get().getPlayersGoals().containsKey(player)) {
            matchingTeam.get().getPlayersGoals().put(player, position);
        } else {
            player.sendMessage("Você já está nesse time ou o time não existe");
        }
    }

    public static void setPlayerTeam(Player player, String team, ChatColor color, Position position) {
        Optional<org.utils.objects.Team> matchingTeam = teams.stream()
                .filter(t -> t.getName().equals(team))
                .findFirst();


        if (matchingTeam.isPresent() && (matchingTeam.get().getPlayersGoals().size() > 3)) {
            player.sendMessage(Color.YELLOW + "Já chegou o numero de maximo de times cadastrados, podemos cadastrar apenas 3 de cada vez");
        } else {
            if (matchingTeam.isPresent() && !matchingTeam.get().getPlayersGoals().containsKey(player)) {
                int teamIndex = teams.indexOf(matchingTeam.get());

                matchingTeam.get().setColor(color);
                matchingTeam.get().getPlayersGoals().put(player, position);

                if (teamIndex != -1) {
                    teams.set(teamIndex, matchingTeam.get());
                }
            } else {
                player.sendMessage(Color.YELLOW + "Time " + team + " já cadastrado, verifique outro nome para time ou veja os nomes indisponiveis em /<times>");
            }
        }
    }

    public static String getPlayerPositionCamp(Player player) {
        Optional<String> teamName = getPlayerTeam(player);
        return teamName.map(s -> Objects.requireNonNull(getTeamByName(s)).getPlayersGoals().get(player).getNamePosition()).orElse(PositionCampEnum.valueOf("DEFAULT")).name();
    }

    public static Optional<org.utils.objects.Team> getColorTeam(String teamName) {
        return teams.stream().filter(t -> Objects.equals(t.getName(), teamName)).findAny();
    }

    public static boolean playerHaveTeam(Player player) {
        if (!teams.isEmpty()) {
            return teams.stream().anyMatch(t -> t.getPlayersGoals()
                    .keySet()
                    .stream()
                    .anyMatch(x -> x.getPlayer() == player));
        }
        return false;
    }

    public static Optional<Team> getTeamColor(String team) {
        return teams.stream()
                .filter(t -> t.getName().equals(team))
                .findFirst();
    }

    public static void strikePlayerWithLightning(Player player) {
        // Obtem a localização atual do jogador
        Location playerLocation = player.getLocation();

        // Obtem o mundo em que o jogador está
        World world = player.getWorld();

        // Faz um raio cair na localização do jogador
        world.strikeLightning(playerLocation);
    }

    public static void giveCursedItem(Player player) {
        ItemStack cursedItem = new ItemStack(Material.DIAMOND_SWORD); // Exemplo de item
        ItemMeta meta = cursedItem.getItemMeta();
        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "cursed_speed", -0.1, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.OFF_HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedModifier);

        // Exemplo: Aumento de dano
        AttributeModifier damageModifier = new AttributeModifier(UUID.randomUUID(), "cursed_damage", -0.4, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_LUCK, damageModifier);

        // Exemplo: Aumento de dano
        AttributeModifier random = new AttributeModifier(UUID.randomUUID(), "cursed_life", -0.8, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, random);

        cursedItem.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        meta.setDisplayName("Item Amaldiçoado"); // Nome do item
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.PROTECTION_FIRE, 100, true);
        meta.setLocalizedName("Os demonios do inferno te deram de presente");
        // Adicionar identificador único, pode ser um lore ou algo similar
        meta.setLore(Arrays.asList("Este item está amaldiçoado"));
        cursedItem.setItemMeta(meta);

        player.getInventory().addItem(cursedItem);
    }

    private static void startSuperStorm(Player player) {
        player.sendMessage(ChatColor.DARK_RED + "Você foi punido, agora você tera efeitos merecidos!");
        player.sendMessage(ChatColor.RED + "Fuja para sobreviver, se você morrer todos seus items serão excluidos para sempre!!");
        World world = player.getWorld();
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(1200); // Duração da tempestade em ticks (5 minutos)
        world.setThunderDuration(1200);

        // Cria raios frequentes e explosões
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                // Raios aleatórios e pequenas explosões
                for (int i = 0; i < 10; i++) {
                    // Escolher uma localização aleatória próxima ao jogador
                    double x = player.getLocation().getX() + (Math.random() - 0.5) * 50;
                    double z = player.getLocation().getZ() + (Math.random() - 0.5) * 50;
                    double y = player.getWorld().getHighestBlockYAt((int) x, (int) z);

                    Location strikeLocation = new Location(player.getWorld(), x, y, z);
                    player.getWorld().strikeLightning(strikeLocation);

                    // Para explosões, você pode adicionar:
                    player.getWorld().createExplosion(strikeLocation, 8F); // Explosão sem fogo
                    player.getWorld().spawnEntity(strikeLocation, EntityType.CREEPER);
                }


            }
        }.runTaskTimer(Futebolistico.instance, 0L, 20L); // Executa a cada segundo

        new BukkitRunnable() {
            @Override
            public void run() {
                world.setStorm(false);
                world.setThundering(false);
                world.setWeatherDuration(0);
                world.setThunderDuration(0);
            }
        }.runTaskLater(Futebolistico.instance, 1200L);
    }

    public static void punishCommandPlayer(Player playerPunish, String command) {
        Calendar calendar = Calendar.getInstance();

        switch (command.toLowerCase()) {
            case "kick" -> playerPunish.kickPlayer(ChatColor.RED + "Você foi chutado do servidor, seja melhor!");
            case "zeus" -> {
                playerPunish.playSound(playerPunish.getLocation(), Sound.AMBIENT_CAVE, 5.0F, 5.0F);
                spawnParticlesNearPlayer(playerPunish, Particle.PORTAL, 2000);
                playerPunish.sendMessage(ChatColor.RED + "Zeus está vendo!");
                strikePlayerWithLightning(playerPunish);
            }
            case "zeus-around" -> {
                World world = playerPunish.getWorld();
                Location center = playerPunish.getLocation();

                applyNauseaEffect(playerPunish);
                // Inicia a chuva
                world.setStorm(true);

                // Raio cai em um círculo de 8 blocos de raio
                int radius = 8;
                for (int i = 0; i < 360; i += 30) { // Aumente o passo para menos raios, diminua para mais
                    double angle = Math.toRadians(i);
                    double x = center.getX() + (radius * Math.cos(angle));
                    double z = center.getZ() + (radius * Math.sin(angle));
                    Location strikeLocation = new Location(world, x, center.getY(), z);
                    world.strikeLightning(strikeLocation);
                }
                playerPunish.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "Você está sendo castigado");
                strikePlayerWithLightning(playerPunish);
            }

            case "curse" -> {
                World world = playerPunish.getWorld();
                playerPunish.playSound(playerPunish.getLocation(), Sound.AMBIENT_CAVE, 5.0F, 5.0F);
                spawnParticlesNearPlayer(playerPunish, Particle.PORTAL, 2000);
                applyNauseaEffect(playerPunish);
                Location center = playerPunish.getLocation();

                List<Entity> entities = new ArrayList<>();
                for (int i = 0; i < 360; i += 18) { // Spawna em um círculo ao redor do jogador
                    double angle = Math.toRadians(i);
                    double x = center.getX() + (5 * Math.cos(angle));
                    double z = center.getZ() + (5 * Math.sin(angle));
                    Location spawnLocation = new Location(world, x, center.getY(), z);

                    Enderman enderman = (Enderman) world.spawnEntity(spawnLocation, EntityType.ENDERMAN);
                    enderman.setAI(false);
                    enderman.teleport(spawnLocation.setDirection(center.toVector().subtract(spawnLocation.toVector())));
                }
                freezePlayer(playerPunish, 60L);
                // Agendar para fazer os Endermans desaparecerem após 4 segundos
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Entity entity : playerPunish.getNearbyEntities(10, 10, 10)) {
                            if (entity instanceof Enderman) {
                                entity.remove();
                            }
                        }

                        entities.forEach(Entity::remove);
                        removeNearbyEndermen(playerPunish, 100);
                    }
                }.runTaskLater(Futebolistico.instance, 50L);
                giveCursedItem(playerPunish);
            }

            case "storm-creeper" -> {
                startSuperStorm(playerPunish);
            }
            case "storm-water-force" -> {
                startWaterStorm(playerPunish, 1);
            }
            case "storm-fire" -> {
                startCustomized(playerPunish, Particle.LAVA, Material.LAVA);
            }
            case "storm-water" -> {
                startCustomized(playerPunish, Particle.FALLING_WATER, Material.WATER);
            }
            case "ban" -> {
                Bukkit.getBanList(BanList.Type.IP).addBan(playerPunish.getServer().getIp(),
                        ChatColor.RED + "Você foi um jogador ruim e agora sofreu a punição geral, melhore!.",
                        null,
                        null);
                playerPunish.kickPlayer(ChatColor.RED + "Você foi banido!");
            }
            case "tempban" -> {
                calendar.add(Calendar.SECOND, 20);
                Bukkit.getBanList(BanList.Type.IP).addBan(playerPunish.getServer().getIp(),
                        ChatColor.RED + "Você foi um jogador ruim e agora sofreu a punição geral, melhore!.",
                        calendar.getTime(),
                        null);
                playerPunish.kickPlayer(ChatColor.RED + "Você foi banido por 20 segundos não faça isso novamente!!");
            }
            case "punish_inventory_temp" -> {
                calendar.add(Calendar.SECOND, 20);
                applyNauseaEffect(playerPunish);
                // Criando uma nova instância de PunishPlayer com a cópia do inventário
                PunishPlayer punishPlayer = new PunishPlayer(playerPunish.getUniqueId(),
                        playerPunish.getInventory(),
                        calendar.getTime(),
                        true,
                        "punish_inventory_temp");

                verifyAndApply(playerPunish, punishPlayer);
                spawnParticlesNearPlayer(playerPunish, Particle.EXPLOSION_NORMAL, 200);
                spawnSoundNearPlayer(playerPunish, Sound.ENTITY_TNT_PRIMED);
                playerPunish.sendMessage(ChatColor.RED + "Todos seus items do inventario foram removidos por conta de alguma violação" +
                        "\n" + ChatColor.YELLOW + "Seus items foram confiscados por 20 segundos" +
                        "\n" + ChatColor.GOLD + "Caso ache que seu banimento é questionavel então fale com o moderador");

                // Limpando o inventário do jogador
                playerPunish.getInventory().clear();

                Bukkit.getScheduler().runTaskLater(Futebolistico.instance, () -> {
                    // Criar baú com os itens copiados
                    createChestWithItems(playerPunish.getPlayer(), getPunishPlayerInventory(punishPlayer.getPlayer()));
                }, 20 * 20L);
            }
            case "punish_inventory" -> {
                playerPunish.getInventory().clear();
                spawnParticlesNearPlayer(playerPunish, Particle.EXPLOSION_NORMAL, 20);
                spawnSoundNearPlayer(playerPunish, Sound.ENTITY_TNT_PRIMED);
                playerPunish.sendMessage(ChatColor.RED + "Todos seus items do inventario foram removidos por conta de alguma violação" +
                        "\n" + ChatColor.YELLOW + "Seus items foram confiscados para sempre" +
                        "\n" + ChatColor.GOLD + "Caso ache que seu banimento é questionavel então fale com o moderador");
            }
            case "flood" -> {
                // Inunda a localização do jogador temporariamente
                floodAreaAroundPlayer(playerPunish);
            }
            case "gravity" -> {
                // Aumenta a gravidade ao redor do jogador, tornando difícil se mover
                applyHeavyGravity(playerPunish);
            }
            case "blindness" -> {
                // Aplica efeito de cegueira ao jogador
                applyBlindnessEffect(playerPunish);
            }
            case "swarm" -> {
                // Spawna uma nuvem de abelhas furiosas ao redor do jogador
                spawnAngryBees(playerPunish);
            }
            case "ice_trap" -> {
                // Prende o jogador em um cubo de gelo
                trapPlayerInIce(playerPunish);
            }
            case "wither_effect" -> {
                // Aplica o efeito Wither ao jogador
                applyWitherEffect(playerPunish);
            }
            case "poisonous_fog" -> {
                // Cria uma névoa venenosa ao redor do jogador
                createPoisonousFog(playerPunish);
            }
            case "zombie_attack" -> {
                // Faz com que Endermen ataquem o jogador
                summonZombieAttack(playerPunish);
            }
            case "skeleton_attack" -> {
                // Faz com que Endermen ataquem o jogador
                summonSkelletonAttack(playerPunish);
            }
        }
    }

    public static void trapPlayerInIce(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        createIceCube(world, loc.clone().add(0, 1, 0)); // Evita prender os pés do jogador no gelo

        // Agendar para derreter o gelo
        Bukkit.getScheduler().runTaskLater(Futebolistico.instance, () -> meltIceCube(world, loc.clone().add(0, 1, 0)), 100L);
    }

    public static void applyHeavyGravity(Player player) {
        int duration = 20 * 30; // 30 segundos, por exemplo
        int amplifier = 3; // Intensidade do efeito de lentidão

        PotionEffect heavyGravityEffect = new PotionEffect(PotionEffectType.SLOW, duration, amplifier);
        player.addPotionEffect(heavyGravityEffect);

        player.sendMessage(ChatColor.DARK_GRAY + "A gravidade ao seu redor aumentou, dificultando seus movimentos!");
    }

    private static void meltIceCube(World world, Location center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location loc = center.clone().add(x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.ICE) {
                        world.getBlockAt(loc).setType(Material.WATER);
                    }
                }
            }
        }
    }

    private static void createIceCube(World world, Location center) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location loc = center.clone().add(x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.AIR) {
                        world.getBlockAt(loc).setType(Material.ICE);
                    }
                }
            }
        }
    }

    public static void applyBlindnessEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1)); // 5 segundos de cegueira
    }

    public static void applyWitherEffect(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 1)); // 5 segundos de efeito Wither
    }

    public static void createPoisonousFog(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();

        Bukkit.getScheduler().runTaskTimer(Futebolistico.instance, () -> {
            world.spawnParticle(Particle.SPELL_MOB, loc, 30, 3, 3, 3, 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
        }, 0L, 20L);

        // Parar após 10 segundos
        Bukkit.getScheduler().runTaskLater(Futebolistico.instance, () -> {
            player.removePotionEffect(PotionEffectType.POISON);
        }, 500L);
    }

    public static void summonZombieAttack(Player player) {
        Location loc = player.getLocation();

        for (int i = 0; i < 5; i++) {
           EntityUtils.createCustomZombie(loc);
        }
    }

    public static void summonSkelletonAttack(Player player) {
        Location loc = player.getLocation();

        for (int i = 0; i < 10; i++) {
            EntityUtils.createCustomSkeleton(loc);
        }
    }

    public static void spawnAngryBees(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();

        for (int i = 0; i < 5; i++) { // Spawnar 5 abelhas
            Bee bee = (Bee) world.spawnEntity(loc, EntityType.BEE);
            bee.setAI(true);
            bee.setAdult();
            bee.setTarget(player);
            bee.setPersistent(true);
        }
    }

    public static void floodAreaAroundPlayer(Player player) {
        Location playerLoc = player.getLocation();
        World world = player.getWorld();

        int radius = 5; // Raio da inundação
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Location loc = playerLoc.clone().add(x, 0, z);
                loc.setY(world.getHighestBlockYAt(loc)); // Encontra o bloco mais alto
                if (loc.getBlock().getType() == Material.AIR) {
                    loc.getBlock().setType(Material.WATER);
                }
            }
        }

        // Agendar para remover a água após um tempo
        Bukkit.getScheduler().runTaskLater(Futebolistico.instance, () -> {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = playerLoc.clone().add(x, 0, z);
                    if (loc.getBlock().getType() == Material.WATER) {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }, 100L); // 5 segundos em ticks
    }

    public static void removeNearbyEndermen(Player player, double radius) {
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Enderman) {
                entity.remove(); // Remove o Enderman
            }
        }
    }

    public static void startCustomized(Player player, Particle particle, Material material) {
        World world = player.getWorld();
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(1200); // Duração da tempestade em ticks (5 minutos)
        world.setThunderDuration(1200);
        player.sendMessage(ChatColor.RED + "Você precisa sobreviver a essa tempestade ou vai perder todos seus items para sempre");
        player.sendMessage(ChatColor.YELLOW + "Boa sorte! :)");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                for (int i = 0; i < 20; i++) {
                    double x = player.getLocation().getX() + (Math.random() - 0.5) * 30;
                    double z = player.getLocation().getZ() + (Math.random() - 0.5) * 30;
                    double y = world.getHighestBlockYAt((int) x, (int) z);

                    Location location = new Location(world, x, y, z);
                    world.spawnParticle(particle, location, 30, 0.5, 0.5, 0.5, 0.01);
                    world.setType(location, material);
                    // Causar dano ou explosão

                    Location strikeLocation = new Location(player.getWorld(), x, y, z);
                    player.getWorld().strikeLightning(strikeLocation);
                    world.createExplosion(strikeLocation, 4.0F, true, true); // Cria uma explosão sem fogo
                }
            }
        }.runTaskTimer(Futebolistico.instance, 0L, 20L); // Executa a cada segundo
    }

    public static void freezePlayer(Player player, long duration) {
        PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, (int) duration, 255, false, false, false);
        player.addPotionEffect(slowness);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }.runTaskLater(Futebolistico.instance, duration);
    }

    private static void applyNauseaEffect(Player player) {
        int duration = 10 * 20; // 10 segundos (20 ticks por segundo)
        int amplifier = 1; // Intensidade do efeito

        PotionEffect nauseaEffect = new PotionEffect(PotionEffectType.CONFUSION, duration, amplifier);
        player.addPotionEffect(nauseaEffect);
    }

    public static List<InventorySave> getPunishPlayerInventory(UUID uuid) {
        Optional<PunishPlayer> first = Futebolistico.punishPlayers.stream().filter(t -> t.getPlayer() == uuid).findFirst();
        return first.map(PunishPlayer::getInventory).orElse(null);
    }

    public static void verifyAndApply(Player player, PunishPlayer punishPlayer) {
        Futebolistico.addPunishPlayer(punishPlayer);
    }

    public static void spawnParticlesNearPlayer(Player player, Particle particle, int qtdParticles) {
        Location location = player.getLocation();

        // Ajuste a localização conforme necessário
        location.add(location.getX(), 1, location.getZ()); // Exemplo: partículas 1 bloco acima do jogador

        // Spawn de partículas
        player.getWorld().spawnParticle(particle, location, qtdParticles); // 10 é o número de partículas
    }

    public static void startWaterStorm(Player player, int durationMinutes) {
        World world = player.getWorld();

        int durationtotal = durationMinutes * 1200;

        player.getWorld().setStorm(true);
        player.getWorld().setThundering(true);
        player.getWorld().setWeatherDuration(durationtotal); // Duração da tempestade em ticks (5 minutos)
        player.getWorld().setThunderDuration(durationtotal);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.getWorld().isThundering()) {
                    this.cancel();
                    return;
                }

                // Gera água em um raio de 20 blocos ao redor do jogador
                for (int i = 0; i < 10; i++) {
                    int dx = (int) (Math.random() * 40) - 20;
                    int dz = (int) (Math.random() * 40) - 20;
                    int x = player.getLocation().getBlockX() + dx;
                    int z = player.getLocation().getBlockZ() + dz;
                    int y = world.getHighestBlockYAt(x, z);

                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();

                    // Verifica se o bloco pode ser substituído por água
                    if (block.getType() == Material.AIR || block.getType() == Material.GRASS) {
                        block.setType(Material.WATER);
                    }
                }
            }
        }.runTaskTimer(Futebolistico.instance, 20L, 20L); // Executa a cada segundo
    }

    public static void spawnSoundNearPlayer(Player player, Sound sound) {
        Location location = player.getLocation();
        // Ajuste a localização conforme necessário
        location.add(location.getX(), 1, location.getZ()); // Exemplo: partículas 1 bloco acima do jogador
        player.playSound(location, sound, 20, 20);
    }

    public static void createChestWithItems(Player player, List<InventorySave> inventory) {
        System.out.println(inventory.toString());
        if (player != null && !inventory.isEmpty()) {
            Location location = player.getLocation();

            // Determinar a direção para onde o jogador está olhando e ajustar a localização
            location = location.add(location.getDirection().multiply(2));
            location.setY(Math.floor(location.getY() + 1)); // Garantir que a localização está no chão

            // Criar o baú
            Block block = location.getBlock();
            block.setType(Material.CHEST);

            // Verificar se o bloco é realmente um baú (para evitar erros)
            if (block.getState() instanceof Chest chest) {
                Inventory chestInventory = chest.getBlockInventory();

                inventory.forEach(t ->
                        chestInventory.addItem(new ItemStack(Material.valueOf(t.getType()), t.getAmount()))
                );

                player.sendMessage(ChatColor.RED + "Seus items foram dropados em ->" + ChatColor.BLUE + " X: "
                        + chestInventory.getLocation().getX()
                        + ChatColor.LIGHT_PURPLE + " Y: " + chestInventory.getLocation().getY() + ChatColor.GREEN + " Z: " + chestInventory.getLocation().getZ());

                player.sendMessage(ChatColor.GOLD + "Seus items vão sumir em 20 segundos");
                Bukkit.getScheduler().runTaskLater(Futebolistico.instance, () -> {
                    chestInventory.clear(); // Limpar o inventário
                    block.setType(Material.AIR); // Remover o baú
                    player.sendMessage(ChatColor.GOLD + "O seu baú foi removido.");
                }, 20 * 20L); // 20 segundos em ticks do servidor
            }
        }

    }

    public void lavaTrap(Player player) {
        UUID playerId = player.getUniqueId();
        lavaTraps.put(playerId, 1); // Inicia com um cubo de tamanho 1

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!lavaTraps.containsKey(playerId)) {
                    this.cancel();
                    return;
                }

                int size = lavaTraps.get(playerId);
                createLavaCube(player.getLocation(), size);

                if (playerMovedOutOfLavaTrap(player.getLocation(), size)) {
                    size *= 2; // Dobrar o tamanho do cubo
                    lavaTraps.put(playerId, size);
                    player.getWorld().strikeLightning(player.getLocation());
                    createLavaCube(player.getLocation(), size);
                }
            }
        }.runTaskTimer(Futebolistico.instance, 0L, 20L); // Executa a cada segundo
    }

    private boolean playerMovedOutOfLavaTrap(Location currentLocation, int size) {
        // Pega as coordenadas do centro do cubo de lava
        double centerX = currentLocation.getX();
        double centerY = currentLocation.getY();
        double centerZ = currentLocation.getZ();

        int halfSize = size / 2;

        // Verifica se o jogador está fora dos limites do cubo de lava
        return currentLocation.getX() > centerX + halfSize ||
                currentLocation.getX() < centerX - halfSize ||
                currentLocation.getY() > centerY + halfSize ||
                currentLocation.getY() < centerY - halfSize ||
                currentLocation.getZ() > centerZ + halfSize ||
                currentLocation.getZ() < centerZ - halfSize;
    }

    private void createLavaCube(Location center, int size) {
        World world = center.getWorld();
        int halfSize = size / 2;

        for (int x = -halfSize; x <= halfSize; x++) {
            for (int y = -halfSize; y <= halfSize; y++) {
                for (int z = -halfSize; z <= halfSize; z++) {
                    if (x == halfSize || x == -halfSize || y == halfSize || y == -halfSize || z == halfSize || z == -halfSize) {
                        Location loc = center.clone().add(x, y, z);
                        world.getBlockAt(loc).setType(Material.LAVA);
                    }
                }
            }
        }
    }

    public void removeLavaTrap(Player player, Location center, int size) {
        UUID playerId = player.getUniqueId();
        if (!lavaTraps.containsKey(playerId)) return;

        World world = center.getWorld();
        int halfSize = size / 2;

        for (int x = -halfSize; x <= halfSize; x++) {
            for (int y = -halfSize; y <= halfSize; y++) {
                for (int z = -halfSize; z <= halfSize; z++) {
                    Location loc = center.clone().add(x, y, z);
                    if (world.getBlockAt(loc).getType() == Material.LAVA) {
                        world.getBlockAt(loc).setType(Material.AIR);
                    }
                }
            }
        }

        lavaTraps.remove(playerId); // Remove o jogador do registro de armadilhas
    }


}
