package dev.twice.astromobsmoney.items;

import dev.twice.astromobsmoney.Main;
import dev.twice.astromobsmoney.utils.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CurrencyItem {
    private final Main plugin;
    private final NamespacedKey currencyKey;
    private final NamespacedKey playerNameKey;

    public CurrencyItem(Main plugin) {
        this.plugin = plugin;
        this.currencyKey = new NamespacedKey(plugin, "currency");
        this.playerNameKey = new NamespacedKey(plugin, "player_name");
    }

    public ItemStack create(Material material, int amount, String playerName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Set data
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(currencyKey, PersistentDataType.INTEGER, amount);

            if (playerName != null && !playerName.isEmpty()) {
                container.set(playerNameKey, PersistentDataType.STRING, playerName);
            }

            String displayName = plugin.getConfig().getString("drop.name");
            if (!displayName.isEmpty()) {

                displayName = displayName
                        .replace("%money", String.valueOf(amount))
                        .replace("%player", playerName);

                displayName = Utils.colorize(displayName);

                meta.setDisplayName(displayName);
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public boolean isCurrencyItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(currencyKey, PersistentDataType.INTEGER);
    }

    public int getAmount(ItemStack item) {
        if (!isCurrencyItem(item)) return 0;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return 0;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        Integer value = container.get(currencyKey, PersistentDataType.INTEGER);
        return value != null ? value : 0;
    }

    public String getPlayerName(ItemStack item) {
        if (!isCurrencyItem(item)) return "";

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return "";

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.getOrDefault(playerNameKey, PersistentDataType.STRING, "");
    }
}