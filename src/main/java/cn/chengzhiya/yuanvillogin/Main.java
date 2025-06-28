package cn.chengzhiya.yuanvillogin;

import cn.chengzhiya.yuanvillogin.command.YuAnvilLogin;
import cn.chengzhiya.yuanvillogin.listener.*;
import cn.chengzhiya.yuanvillogin.manager.*;
import cn.chengzhiya.yuanvillogin.manager.config.ConfigManager;
import cn.chengzhiya.yuanvillogin.task.AutoClose;
import cn.chengzhiya.yuanvillogin.task.TakeBanTime;
import cn.chengzhiya.yuanvillogin.util.message.LogUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;

    private LibrariesManager librariesManager;
    private AdventureManager adventureManager;
    private ConfigManager configManager;
    private CacheManager cacheManager;
    private BungeeCordManager bungeeCordManager;
    private PluginHookManager pluginHookManager;

    @Override
    public void onLoad() {
        instance = this;

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.adventureManager = new AdventureManager();
        this.adventureManager.init();

        this.configManager = new ConfigManager();
        this.configManager.init();

        this.cacheManager = new CacheManager();
        this.cacheManager.init();

        this.bungeeCordManager = new BungeeCordManager();
        this.bungeeCordManager.init();

        this.pluginHookManager = new PluginHookManager();
        this.pluginHookManager.hook();

        Bukkit.getPluginManager().registerEvents(new BanPlayer(), this);
        Bukkit.getPluginManager().registerEvents(new ForceOpenMenu(), this);
        Bukkit.getPluginManager().registerEvents(new Login(), this);
        Bukkit.getPluginManager().registerEvents(new Menu(), this);
        Bukkit.getPluginManager().registerEvents(new OpenMenu(), this);

        Objects.requireNonNull(getCommand("yuanvillogin")).setExecutor(new YuAnvilLogin());
        Objects.requireNonNull(getCommand("yuanvillogin")).setTabCompleter(new YuAnvilLogin());

        new AutoClose().runTaskTimerAsynchronously(this, 0L, 20L);
        new TakeBanTime().runTaskTimerAsynchronously(this, 0L, 20L);

        LogUtil.log("&e-----------&6=&e鱼の铁砧登录&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方售后群: 1014337508");
        LogUtil.log("&e-----------&6=&e鱼の铁砧登录&6=&e-----------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (this.pluginHookManager != null) {
            this.pluginHookManager.unhook();
        }
        if (this.bungeeCordManager != null) {
            this.bungeeCordManager.close();
        }
        if (this.cacheManager != null) {
            this.cacheManager.close();
        }

        LogUtil.log("&e-----------&6=&e鱼の铁砧登录&6=&e-----------");
        LogUtil.log("&a插件卸载成功! 官方售后群: 1014337508");
        LogUtil.log("&e-----------&6=&e鱼の铁砧登录&6=&e-----------");

        if (this.adventureManager != null) {
            this.adventureManager.close();
        }

        this.pluginHookManager = null;
        this.bungeeCordManager = null;
        this.cacheManager = null;
        this.librariesManager = null;

        instance = null;
    }
}
