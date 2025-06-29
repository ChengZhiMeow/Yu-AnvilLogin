package cn.chengzhiya.yuanvillogin.util;

import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import cn.chengzhiya.yuanvillogin.util.reflection.ReflectionUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class InventoryUtil {
    /**
     * 获取玩家打开的菜单
     *
     * @param player 玩家实例
     * @return 菜单实例
     */
    public static Inventory getOpenInventory(Player player) {
        try {
            Object inventoryView = ReflectionUtil.invokeMethod(
                    ReflectionUtil.getMethod(HumanEntity.class, "getOpenInventory", true),
                    player
            );

            if (inventoryView == null) {
                return null;
            }

            return ReflectionUtil.invokeMethod(
                    ReflectionUtil.getMethod(inventoryView.getClass(), "getTopInventory", true),
                    inventoryView
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检测指定玩家实例是否已经打开了登录菜单
     *
     * @param player 玩家实例
     * @return 结果
     */
    public static boolean isOpenLoginMenu(Player player) {
        try {
            Inventory inventory = getOpenInventory(player);
            if (inventory == null) {
                return false;
            }

            return inventory.getHolder() instanceof AbstractMenu;
        } catch (Exception e) {
            return false;
        }
    }
}
