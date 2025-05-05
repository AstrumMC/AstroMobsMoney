package dev.twice.astromobsmoney.drops;

import dev.twice.astromobsmoney.Main;
import dev.twice.astromobsmoney.items.CurrencyItem;
import dev.twice.astromobsmoney.utils.MathUtil;
import dev.twice.astromobsmoney.utils.Utils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

@Getter
public class DropService {
    private final Main plugin;
    private final CurrencyItem currencyItem;

    public DropService(Main plugin) {
        this.plugin = plugin;
        this.currencyItem = new CurrencyItem(plugin);
    }

    public Item processDeath(LivingEntity entity, Player killer) {
        if (entity == null || killer == null) return null;

        int reward = 0;
        String playerName = "";

        if (entity instanceof Player) {
            Player killed = (Player) entity;

            double playerPercent = plugin.getRegistry().getPlayerMoneyPercent();
            reward = (int)(Main.econ.getBalance(killed) * playerPercent);

            if (reward > 0) {
                Main.econ.withdrawPlayer(killed, reward);
            }

            playerName = killed.getName();

            sendDeathMessage(killed, killer, reward);
        } else if (entity instanceof Monster) {
            reward = plugin.getEntityRewardService().calculateReward(entity, killer);
        } else if (entity instanceof Animals) {
            reward = plugin.getEntityRewardService().calculateReward(entity, killer);
        } else {
            return null;
        }

        if (reward <= 0) return null;

        plugin.getEntityRewardService().trackKill(killer, entity);

        return createDrop(reward, playerName, entity.getLocation());
    }

    private void sendDeathMessage(Player killed, Player killer, int money) {
        Location location = killed.getLocation();
        String message = plugin.getConfig().getString("messages.death")
                .replace("%player", killer.getName())
                .replace("%money", String.valueOf(money))
                .replace("%x", String.valueOf(location.getBlockX()))
                .replace("%y", String.valueOf(location.getBlockY()))
                .replace("%z", String.valueOf(location.getBlockZ()));

        killed.sendMessage(Utils.colorize(message));
    }

    private Item createDrop(int amount, String playerName, Location location) {
        Material material = plugin.getRegistry().getDropMaterial();
        ItemStack item = currencyItem.create(material, amount, playerName);
        Item droppedItem = location.getWorld().dropItemNaturally(location, item);

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            droppedItem.setCustomNameVisible(true);
            droppedItem.setCustomName(item.getItemMeta().getDisplayName());
        }

        MathUtil.setDespawnTimer(droppedItem);

        return droppedItem;
    }
}