package dev.twice.astromobsmoney.drops;

import dev.twice.astromobsmoney.Main;
import dev.twice.astromobsmoney.items.CurrencyItem;
import dev.twice.astromobsmoney.utils.Utils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class DropProcessor {
    private final Main plugin;
    private final CurrencyItem currencyItem;

    public void processPickup(Player player, ItemStack item) {
        if (!currencyItem.isCurrencyItem(item)) return;

        int amount = currencyItem.getAmount(item) * item.getAmount();
        String killedPlayerName = currencyItem.getPlayerName(item);
        double boost = 1.0;

        if (killedPlayerName.isEmpty()) {

            String playerGroup = Main.perms.getPrimaryGroup(player);
            boost = plugin.getConfig().getDouble("boost-money." + playerGroup,
                    plugin.getConfig().getDouble("boost-money.default", 1.0));
        }

        final double finalAmount = amount * boost;

        CompletableFuture.runAsync(() -> {
            Main.econ.depositPlayer(player, finalAmount);
        });

        String messageKey;
        if (!killedPlayerName.isEmpty()) {
            messageKey = "action-bar-pickup-player";
        } else if (boost == 1.0) {
            messageKey = "action-bar-pickup-mob";
        } else {
            messageKey = "action-bar-pickup-mob-boost";
        }

        String message = plugin.getConfig().getString("messages." + messageKey)
                .replace("%player", killedPlayerName)
                .replace("%money", String.valueOf(amount))
                .replace("%boost", String.valueOf(finalAmount - amount));

        message = Utils.colorize(message);
        player.sendActionBar(Component.text(message));
    }
}