package cn.chengzhiya.yuanvillogin.hook;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.listener.ChangeAnvilItemName;
import cn.chengzhiya.yuanvillogin.listener.FakePlayerInventory;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public final class PacketEventsHook extends AbstractHook {
    private ServerVersion serverVersion;

    /**
     * 初始化PacketEvents的API
     */
    @Override
    public void hook() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(Main.instance));
        PacketEvents.getAPI().getSettings()
                .bStats(false)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .debug(false)
                .timeStampMode(TimeStampMode.NANO);
        PacketEvents.getAPI().load();

        PacketEvents.getAPI().init();
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        super.enable = true;

        this.registerListener(
                new ChangeAnvilItemName(),
                PacketListenerPriority.NORMAL
        );

        this.registerListener(
                new FakePlayerInventory(),
                PacketListenerPriority.NORMAL
        );
    }

    /**
     * 卸载PacketEvents的API
     */
    @Override
    public void unhook() {
        PacketEvents.getAPI().terminate();
        super.enable = false;
    }

    /**
     * 给指定用户实例发送指定数据包
     *
     * @param user   接收数据包的用户实例
     * @param packet 发送的数据包
     */
    public void sendPacket(User user, PacketWrapper<?> packet) {
        if (super.enable) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(user, packet);
        }
    }

    /**
     * 给指定玩家实例发送指定数据包
     *
     * @param player 接收数据包的玩家实例
     * @param packet 发送的数据包
     */
    public void sendPacket(Player player, PacketWrapper<?> packet) {
        if (super.enable) {
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        }
    }

    /**
     * 注册监听器
     *
     * @param packetListener 数据包监听器实例
     * @param priority       监听器权重
     */
    public void registerListener(PacketListener packetListener, PacketListenerPriority priority) {
        PacketEvents.getAPI().getEventManager().registerListener(packetListener, priority);
    }
}