package dev.twice.astromobsmoney.utils;

import dev.twice.astromobsmoney.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MathUtil {
    private static final int DESPAWN_TIME = 60 * 20;
    private static final Map<UUID, BukkitTask> despawnTasks = new ConcurrentHashMap<>();

    public static void setDespawnTimer(Item droppedItem) {
        if (droppedItem == null) return;

        UUID itemId = droppedItem.getUniqueId();

        BukkitTask existingTask = despawnTasks.remove(itemId);
        if (existingTask != null) existingTask.cancel();


        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {

                Entity entity = Bukkit.getEntity(itemId);
                if (entity != null && entity.isValid() && !entity.isDead()) {
                    entity.remove();
                }
                despawnTasks.remove(itemId);
            }
        }.runTaskLater(Main.getInstance(), DESPAWN_TIME);

        despawnTasks.put(itemId, task);
    }

    public static void cleanup() {
        despawnTasks.values().forEach(BukkitTask::cancel);
        despawnTasks.clear();
    }
}