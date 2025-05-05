package dev.twice.astromobsmoney.entities;

import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class EntityStats {
    private final Map<EntityType, AtomicInteger> kills = new ConcurrentHashMap<>();
    private final AtomicInteger totalEarnings = new AtomicInteger(0);

    public void incrementKill(EntityType type) {
        kills.computeIfAbsent(type, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void addEarnings(int amount) {
        totalEarnings.addAndGet(amount);
    }

    public int getKills(EntityType type) {
        AtomicInteger count = kills.get(type);
        return count != null ? count.get() : 0;
    }

    public Map<EntityType, Integer> getAllKills() {
        Map<EntityType, Integer> result = new HashMap<>();
        kills.forEach((type, count) -> result.put(type, count.get()));
        return result;
    }

    public int getTotalKills() {
        return kills.values().stream().mapToInt(AtomicInteger::get).sum();
    }

    public int getTotalEarnings() {
        return totalEarnings.get();
    }
}