package dev.twice.astromobsmoney.entities;

import dev.twice.astromobsmoney.Main;
import dev.twice.astromobsmoney.registry.ValueRange;
import lombok.Getter;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EntityRewardService {
    private final Main plugin;
    private final Map<UUID, EntityStats> playerStats = new ConcurrentHashMap<>();

    public EntityRewardService(Main plugin) {
        this.plugin = plugin;
    }

    public int calculateReward(Entity entity, Player killer) {
        if (entity == null) return 0;

        if (entity instanceof Player) {
            double percentage = plugin.getRegistry().getPlayerMoneyPercent();

            return 0;
        } else if (entity instanceof Monster) {
            ValueRange range = plugin.getRegistry().getMonsterValue(entity.getType());
            return range != null ? range.getRandom() : 0;
        } else if (entity instanceof Animals) {
            ValueRange range = plugin.getRegistry().getAnimalValue(entity.getType());
            return range != null ? range.getRandom() : 0;
        }

        return 0;
    }

    public void trackKill(Player player, Entity killed) {
        EntityStats stats = playerStats.computeIfAbsent(player.getUniqueId(), id -> new EntityStats());
        stats.incrementKill(killed.getType());
    }

    public int getKillCount(Player player, EntityType type) {
        EntityStats stats = playerStats.get(player.getUniqueId());
        return stats != null ? stats.getKills(type) : 0;
    }

    public Map<EntityType, Integer> getAllKills(Player player) {
        EntityStats stats = playerStats.get(player.getUniqueId());
        return stats != null ? stats.getAllKills() : new HashMap<>();
    }
}