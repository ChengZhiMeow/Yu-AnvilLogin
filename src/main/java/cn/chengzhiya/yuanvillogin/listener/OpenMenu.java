package cn.chengzhiya.yuanvillogin.listener;

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
        openMenu(event.getPlayer());
    }

    @EventHandler
    public void onPlayerResourcePackStatus(PlayerResourcePackStatusEvent status) {
        if (!ConfigUtil.getConfig().getBoolean("openMenu.resourceLoadDone")) {
            return;
        }
        Player player = status.getPlayer();
        if (status.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            openMenu(player);
        }
    }

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
