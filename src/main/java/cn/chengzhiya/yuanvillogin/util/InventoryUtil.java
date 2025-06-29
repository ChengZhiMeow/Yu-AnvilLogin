package cn.chengzhiya.yuanvillogin.util;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import cn.chengzhiya.yuanvillogin.util.reflection.ReflectionUtil;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InventoryUtil {
    /**
     * 检测指定玩家实例是否已经打开了登录菜单
     *
     * @param player 玩家实例
     * @return 结果
     */
    public static boolean isOpenLoginMenu(Player player) {
        Object inventoryView;
        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                .isNewerThanOrEquals(ServerVersion.V_1_21)
        ) {
            inventoryView = player.getOpenInventory();
        } else {
            inventoryView = ReflectionUtil.invokeMethod(
                    ReflectionUtil.getMethod(Player.class, "getOpenInventory", true),
                    player
            );
        }

        if (inventoryView == null) {
            return false;
        }

        Inventory inventory = ReflectionUtil.invokeMethod(
                ReflectionUtil.getMethod(inventoryView.getClass(), "getTopInventory", true),
                inventoryView
        );

        if (inventory == null) {
            return false;
        }

        return inventory.getHolder() instanceof AbstractMenu;
    }
}
