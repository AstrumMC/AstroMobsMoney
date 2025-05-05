package dev.twice.astromobsmoney.utils;

import dev.twice.astromobsmoney.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncUtils {

    public static <T> CompletableFuture<Void> runAsyncThenSync(Supplier<T> asyncTask, Consumer<T> callback) {
        return CompletableFuture.supplyAsync(asyncTask)
                .thenAccept(result -> {
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> callback.accept(result));
                });
    }

    public static BukkitTask runLater(Runnable task, long delayTicks) {
        return Bukkit.getScheduler().runTaskLater(Main.getInstance(), task, delayTicks);
    }

    public static BukkitTask runLaterAsync(Runnable task, long delayTicks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), task, delayTicks);
    }

    public static BukkitTask runRepeatingAsync(Runnable task, long delayTicks, long periodTicks) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), task, delayTicks, periodTicks);
    }
}