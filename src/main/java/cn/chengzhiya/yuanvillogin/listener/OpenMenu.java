package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.LoginMenu;
import cn.chengzhiya.yuanvillogin.menu.RegisterMenu;
import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public final class OpenMenu implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ConfigUtil.getConfig().getBoolean("openMenu.join")) {
            return;
        }
        Player player = event.getPlayer();

        if (Main.instance.getPluginHookManager().getAuthmeHook().isLoggedIn(player)) {
            return;
        }

        MHDFScheduler.getAsyncScheduler().runTaskLater(Main.instance, () -> openMenu(player),
                ConfigUtil.getConfig().getInt("openMenu.delay")
        );
    }

    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (!ConfigUtil.getConfig().getBoolean("openMenu.resourceLoadDone")) {
            return;
        }
        Player player = event.getPlayer();

        if (Main.instance.getPluginHookManager().getAuthmeHook().isLoggedIn(player)) {
            return;
        }

        if (event.getStatus() != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            return;
        }

        MHDFScheduler.getAsyncScheduler().runTaskLater(Main.instance, () -> openMenu(player),
                ConfigUtil.getConfig().getInt("openMenu.delay")
        );
    }

    /**
     * 打开菜单
     *
     * @param player 玩家实例
     */
    private void openMenu(Player player) {
        if (Main.instance.getPluginHookManager().getAuthmeHook()
                .isRegistered(player)
        ) {
            new LoginMenu(player, null).openMenu();
            return;
        }
        new RegisterMenu(player, null).openMenu();
    }
}
