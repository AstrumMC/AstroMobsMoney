package dev.twice.astromobsmoney.api;

import dev.twice.astromobsmoney.Main;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EconomyManager {
    private final Main plugin;
    private final Map<String, Double> boostCache = new ConcurrentHashMap<>();

    public EconomyManager(Main plugin) {
        this.plugin = plugin;

        ConfigurationSection boostSection = plugin.getConfig().getConfigurationSection("boost-money");
        if (boostSection != null) {
            boostSection.getKeys(false).forEach(key -> {
                double boost = boostSection.getDouble(key);
                boostCache.put(key, boost);
            });
        }
    }

    public double getPlayerBoost(Player player) {
        String group = Main.perms.getPrimaryGroup(player);
        return boostCache.getOrDefault(group, 1.0);
    }

    public void depositAsync(Player player, double amount) {
        CompletableFuture.runAsync(() -> Main.econ.depositPlayer(player, amount));
    }

    public void withdrawAsync(Player player, double amount) {
        CompletableFuture.runAsync(() -> Main.econ.withdrawPlayer(player, amount));
    }

    public double getBalance(Player player) {
        return Main.econ.getBalance(player);
    }
}