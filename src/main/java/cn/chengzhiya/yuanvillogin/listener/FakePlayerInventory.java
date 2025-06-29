package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import cn.chengzhiya.yuanvillogin.util.InventoryUtil;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCursorItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class FakePlayerInventory implements PacketListener {
    @Getter
    private static final HashMap<String, Inventory> fakePlayerInventoryHashMap = new HashMap<>();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();

        // 处理点击
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            Inventory fakeInventory = getFakePlayerInventoryHashMap().get(player.getName());
            if (fakeInventory == null) {
                return;
            }
            handleClickFakePlayerInventory(player, event);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Player player = event.getPlayer();

        // 拦截并修改打开菜单
        if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
            Inventory fakeInventory = getFakePlayerInventoryHashMap().get(player.getName());
            if (fakeInventory == null) {
                return;
            }
            handleOpenFakePlayerInventory(player, fakeInventory, event);
        }
    }

    /**
     * 处理点击虚假玩家背包数据包
     *
     * @param player 玩家实例
     * @param event  数据包事件实例
     */
    private void handleClickFakePlayerInventory(Player player, PacketReceiveEvent event) {
        WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
        if (packet.getSlot() <= 2) {
            return;
        }

        if (!InventoryUtil.isOpenLoginMenu(player)) {
            return;
        }

        Inventory inventory = InventoryUtil.getOpenInventory(player);
        if (inventory == null) {
            return;
        }

        ItemStack item = packet.getCarriedItemStack();
        if (item != null && item.getAmount() >= 1) {
            MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () ->
                    ((AbstractMenu) inventory.getHolder()).clickItem(SpigotConversionUtil.toBukkitItemStack(item))
            );
            handleSendFakeCursorItem(player, item);
        }
    }

    /**
     * 处理发送虚假目标物品数据包
     *
     * @param player 玩家实例
     * @param item   物品实例
     */
    private void handleSendFakeCursorItem(Player player, ItemStack item) {
        WrapperPlayServerSetCursorItem packet = new WrapperPlayServerSetCursorItem(item);
        Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player, packet);
    }

    /**
     * 处理打开虚假玩家背包数据包
     *
     * @param player        玩家实例
     * @param fakeInventory 虚假背包实例
     * @param event         数据包事件实例
     */
    private void handleOpenFakePlayerInventory(Player player, Inventory fakeInventory, PacketSendEvent event) {
        WrapperPlayServerWindowItems packet = new WrapperPlayServerWindowItems(event);

        List<ItemStack> itemList = new ArrayList<>();

        List<ItemStack> packetItemList = packet.getItems();
        if (!InventoryUtil.isOpenLoginMenu(player)) {
            return;
        }

        Inventory inventory = InventoryUtil.getOpenInventory(player);
        if (inventory == null) {
            return;
        }

        for (int i = 0; i < inventory.getContents().length; i++) {
            itemList.add(packetItemList.get(i));
        }

        for (org.bukkit.inventory.ItemStack itemStack : fakeInventory.getContents()) {
            if (itemStack == null) {
                itemList.add(ItemStack.EMPTY);
                continue;
            }

            itemList.add(SpigotConversionUtil.fromBukkitItemStack(itemStack));
        }

        packet.setItems(itemList);

        event.setLastUsedWrapper(packet);
        event.markForReEncode(true);
    }
}
