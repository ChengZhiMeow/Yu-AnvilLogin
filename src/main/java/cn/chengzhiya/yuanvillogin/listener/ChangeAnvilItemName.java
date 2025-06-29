package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.yuanvillogin.builder.ItemStackBuilder;
import cn.chengzhiya.yuanvillogin.menu.AbstractMenu;
import cn.chengzhiya.yuanvillogin.util.AnvilUtil;
import cn.chengzhiya.yuanvillogin.util.InventoryUtil;
import cn.chengzhiya.yuanvillogin.util.menu.MenuUtil;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientNameItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

public final class ChangeAnvilItemName implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketType() == PacketType.Play.Client.NAME_ITEM) {
            handleNameItem(player, event);
        }
    }

    /**
     * 处理重命名数据包
     *
     * @param player 玩家实例
     * @param event  数据包事件实例
     */
    private void handleNameItem(Player player, PacketReceiveEvent event) {
        WrapperPlayClientNameItem packet = new WrapperPlayClientNameItem(event);
        AnvilUtil.getAnvilInputHashMap().put(player.getName(), packet.getItemName().replace(" ", ""));

        if (!InventoryUtil.isOpenLoginMenu(player)) {
            return;
        }

        Inventory inventory = InventoryUtil.getOpenInventory(player);
        if (inventory == null) {
            return;
        }

        AbstractMenu menu = (AbstractMenu) inventory.getHolder();
        if (menu == null) {
            return;
        }

        ConfigurationSection items = menu.getConfig().getConfigurationSection("items");
        if (items == null) {
            return;
        }

        ConfigurationSection item = items.getConfigurationSection("结果");
        if (item == null) {
            return;
        }

        ItemStackBuilder itemStackBuilder = MenuUtil.getMenuItemStackBuilder(player, item, "结果")
                .persistentDataContainer("type", PersistentDataType.STRING, "items");

        String id = item.getString("id");
        if (id != null) {
            itemStackBuilder.persistentDataContainer("id", PersistentDataType.STRING, id);
        }

        inventory.setItem(
                2,
                itemStackBuilder.build()
        );
        player.updateInventory();
    }
}
