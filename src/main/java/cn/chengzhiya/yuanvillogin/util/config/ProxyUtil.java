package cn.chengzhiya.yuanvillogin.util.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

public final class ProxyUtil {
    private static final File file = new File(ConfigUtil.getDataFolder(), "proxy.yml");
    private static YamlConfiguration data;

    /**
     * 保存初始代理文件
     */
    public static void saveDefaultProxy() {
        FileUtil.saveResource("proxy.yml", "proxy_zh.yml", false);
    }

    /**
     * 加载代理文件
     */
    public static void reloadProxy() {
        data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取代理文件配置文件实例
     */
    public static YamlConfiguration getProxyConfig() {
        if (data == null) {
            reloadProxy();
        }
        return data;
    }

    /**
     * 获取代理实例
     *
     * @return 代理实例
     */
    public static Proxy getProxy() {
        ConfigurationSection config = ProxyUtil.getProxyConfig().getConfigurationSection("proxySettings");
        if (config != null) {
            if (config.getBoolean("enable")) {
                String type = config.getString("type");
                String host = config.getString("host");
                int port = config.getInt("port");

                return new Proxy(Proxy.Type.valueOf(type), new InetSocketAddress(Objects.requireNonNull(host), port));
            }
        }

        return Proxy.NO_PROXY;
    }
}
