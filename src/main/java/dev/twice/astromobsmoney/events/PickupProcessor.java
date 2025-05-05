package dev.twice.astromobsmoney.events;

import dev.twice.astromobsmoney.Main;
import dev.twice.astromobsmoney.drops.DropProcessor;
import dev.twice.astromobsmoney.items.CurrencyItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class PickupProcessor implements Listener {
    private final Main plugin;
    private final CurrencyItem currencyItem;
    private final DropProcessor dropProcessor;

    public PickupProcessor(Main plugin) {
        this.plugin = plugin;
        this.currencyItem = new CurrencyItem(plugin);
        this.dropProcessor = new DropProcessor(plugin, currencyItem);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPickup(PlayerAttemptPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();

        if (!currencyItem.isCurrencyItem(itemStack)) return;

        event.setCancelled(true);
        event.getItem().remove();

        dropProcessor.processPickup(event.getPlayer(), itemStack);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryPickup(InventoryPickupItemEvent event) {

        if (event.getInventory().getType() == InventoryType.PLAYER) return;

        ItemStack itemStack = event.getItem().getItemStack();
        if (currencyItem.isCurrencyItem(itemStack)) {
            event.setCancelled(true);
            event.getItem().setPickupDelay(100);
        }
    }
}