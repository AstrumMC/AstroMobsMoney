package dev.twice.astromobsmoney.registry;

import dev.twice.astromobsmoney.Main;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Registry {
    private final Main plugin;

    private final Map<String, Object> configCache = new ConcurrentHashMap<>();
    private final Map<String, ConfigEntry<?>> customData = new ConcurrentHashMap<>();

    private final Map<EntityType, ValueRange> monsterValues = new ConcurrentHashMap<>();
    private final Map<EntityType, ValueRange> animalValues = new ConcurrentHashMap<>();

    private ValueRange defaultMonsterValue;
    private ValueRange defaultAnimalValue;

    private Material dropMaterial;
    private String dropName;
    private double playerMoneyPercent;

    public Registry(Main plugin) {
        this.plugin = plugin;
        loadConfiguration();
    }

    private void loadConfiguration() {

        this.dropMaterial = Material.valueOf(plugin.getConfig().getString("drop.material").toUpperCase());
        this.dropName = plugin.getConfig().getString("drop.name");
        this.playerMoneyPercent = plugin.getConfig().getDouble("money.player");

        ConfigurationSection monsterSection = plugin.getConfig().getConfigurationSection("money.monster");
        if (monsterSection != null) {
            for (String key : monsterSection.getKeys(false)) {
                int min = monsterSection.getInt(key + ".min", 0);
                int max = monsterSection.getInt(key + ".max", 0);

                if (key.equalsIgnoreCase("default")) {
                    defaultMonsterValue = new ValueRange(min, max);
                } else {
                    try {
                        EntityType type = EntityType.valueOf(key.toUpperCase());
                        monsterValues.put(type, new ValueRange(min, max));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }

        ConfigurationSection animalSection = plugin.getConfig().getConfigurationSection("money.animal");
        if (animalSection != null) {
            for (String key : animalSection.getKeys(false)) {
                int min = animalSection.getInt(key + ".min", 0);
                int max = animalSection.getInt(key + ".max", 0);

                if (key.equalsIgnoreCase("default")) {
                    defaultAnimalValue = new ValueRange(min, max);
                } else {
                    try {
                        EntityType type = EntityType.valueOf(key.toUpperCase());
                        animalValues.put(type, new ValueRange(min, max));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
    }

    public <T> void registerData(String key, ConfigEntry<T> entry) {
        customData.put(key, entry);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        ConfigEntry<?> entry = customData.get(key);
        return entry != null ? (T) entry.getValue() : null;
    }

    public ValueRange getMonsterValue(EntityType type) {
        return monsterValues.getOrDefault(type, defaultMonsterValue);
    }

    public ValueRange getAnimalValue(EntityType type) {
        return animalValues.getOrDefault(type, defaultAnimalValue);
    }
}