package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public final class ForceOpenMenu implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!event.isCancelled()) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof AbstractMenu)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        if (Main.instance.getPluginHookManager().getAuthmeHook().isLoggedIn(player)) {
            return;
        }

        event.setCancelled(false);
    }
}
