package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.util.AnvilUtil;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPlayerInventory;
import fr.xephi.authme.events.LoginEvent;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class Login implements Listener {
    @Getter
    private static final List<String> noBlackList = new ArrayList<>();

    @EventHandler
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();

        AnvilUtil.getAnvilInputHashMap().remove(player.getName());
        FakePlayerInventory.getFakePlayerInventoryHashMap().remove(player.getName());
        player.closeInventory();

        // 使用数据包将玩家物品真实发送回去
        {
            org.bukkit.inventory.ItemStack[] contents = player.getInventory().getContents();
            for (int i = 0; i < contents.length; i++) {
                org.bukkit.inventory.ItemStack item = contents[i];

                ItemStack itemStack;
                if (item == null) {
                    itemStack = ItemStack.EMPTY;
                } else {
                    itemStack = SpigotConversionUtil.fromBukkitItemStack(item);
                }

                WrapperPlayServerSetPlayerInventory packet = new WrapperPlayServerSetPlayerInventory(
                        i,
                        itemStack
                );

                Main.instance.getPluginHookManager().getPacketEventsHook().sendPacket(player, packet);
            }
        }
    }
}
