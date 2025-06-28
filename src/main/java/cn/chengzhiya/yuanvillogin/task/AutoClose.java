package cn.chengzhiya.yuanvillogin.task;

import cn.chengzhiya.mhdfscheduler.runnable.MHDFRunnable;
import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class AutoClose extends MHDFRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Main.instance.getPluginHookManager().getAuthmeHook().isLoggedIn(player)) {
                continue;
            }

            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof AbstractMenu)) {
                continue;
            }

            MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, player::closeInventory);
        }
    }
}
