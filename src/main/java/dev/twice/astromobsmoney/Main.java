package dev.twice.astromobsmoney;

import dev.twice.astromobsmoney.api.EconomyManager;
import dev.twice.astromobsmoney.drops.DropService;
import dev.twice.astromobsmoney.entities.EntityRewardService;
import dev.twice.astromobsmoney.events.EntityDeathProcessor;
import dev.twice.astromobsmoney.events.PickupProcessor;
import dev.twice.astromobsmoney.registry.Registry;
import dev.twice.astromobsmoney.utils.MathUtil;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    private static Main instance;
    public static Economy econ;
    public static Permission perms;

    private Registry registry;
    private DropService dropService;
    private EntityRewardService entityRewardService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if (!setupEconomy() || !setupPermissions()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.registry = new Registry(this);
        this.entityRewardService = new EntityRewardService(this);
        this.dropService = new DropService(this);

        getServer().getPluginManager().registerEvents(new EntityDeathProcessor(this), this);
        getServer().getPluginManager().registerEvents(new PickupProcessor(this), this);
    }

    @Override
    public void onDisable() {
        MathUtil.cleanup();
    }

    public static Main getInstance() {
        return instance;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return true;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) return false;
        perms = rsp.getProvider();
        return true;
    }
}