package cn.chengzhiya.yuanvillogin.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public interface Menu {
    /**
     * 触发打开菜单事件的时候
     *
     * @param event 触发打开菜单事件
     */
    void open(InventoryOpenEvent event);

    /**
     * 触发点击菜单事件的时候
     *
     * @param event 触发点击菜单事件
     */
    void click(InventoryClickEvent event);

    /**
     * 触发关闭菜单事件的时候
     *
     * @param event 触发关闭菜单事件
     */
    void close(InventoryCloseEvent event);

    /**
     * 获取玩家背包
     *
     * @return 玩家背包
     */
    Inventory getPlayerInventory();
}
