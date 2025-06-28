package cn.chengzhiya.yuanvillogin.manager;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import cn.chengzhiya.yuanvillogin.util.message.LogUtil;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public final class BungeeCordManager {
    /**
     * 初始化群组模式
     */
    public void init() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().registerOutgoingPluginChannel(Main.instance, "BungeeCord");
        }
    }

    /**
     * 关闭群组模式
     */
    public void close() {
        if (isBungeeCordMode()) {
            getServer().getMessenger().unregisterOutgoingPluginChannel(Main.instance, "BungeeCord");
        }
    }

    /**
     * 检测是否开启群组模式
     *
     * @return 结果
     */
    public boolean isBungeeCordMode() {
        YamlConfiguration spigot = null;
        try {
            spigot = Bukkit.getServer().spigot().getSpigotConfig();
        } catch (NoSuchMethodError e) {
            File serverFolder = ConfigUtil.getDataFolder().getParentFile().getParentFile();
            File spigotFile = new File(serverFolder, "spigot.yml");
            if (spigotFile.exists()) {
                spigot = YamlConfiguration.loadConfiguration(spigotFile);
            }
        }

        if (spigot == null) {
            return false;
        }
        return spigot.getBoolean("settings.bungeecord");
    }

    /**
     * 给BC插件通道发送指定消息数据实例
     *
     * @param out 消息数据实例
     */
    private void sendPluginMessage(ByteArrayDataOutput out) {
        if (!isBungeeCordMode()) {
            LogUtil.debug("发送插件消息失败 | 原因: {}",
                    "未开启群组模式"
            );
            return;
        }

        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (playerList.isEmpty()) {
            LogUtil.debug("发送插件消息失败 | 原因: {}",
                    "服务器没有玩家"
            );
            return;
        }

        playerList.get(0).sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
    }

    /**
     * 将指定玩家ID的玩家移动到指定服务器ID的服务器
     *
     * @param playerName 玩家ID
     * @param serverName 服务器ID
     */
    public void connectServer(String playerName, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(playerName);
        out.writeUTF(serverName);

        sendPluginMessage(out);
    }
}
