package cn.chengzhiya.yuanvillogin.util.config;

import cn.chengzhiya.yuanvillogin.util.message.MessageUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public final class MenuConfigUtil {
    @Getter
    private static final File menuFolder = new File(ConfigUtil.getDataFolder(), "menu");
    @Getter
    private static final ConcurrentHashMap<String, YamlConfiguration> menuHashMap = new ConcurrentHashMap<>();

    /**
     * 保存初始自定义菜单
     */
    public static void saveDefaultMenu() {
        FileUtil.createFolder(getMenuFolder());
        FileUtil.saveResource("menu/login.yml", "menu/login.yml", false);
        FileUtil.saveResource("menu/register.yml", "menu/register.yml", false);
    }

    /**
     * 加载菜单
     */
    public static void reloadMenu() {
        getMenuHashMap().clear();
        for (File file : FileUtil.listFiles(getMenuFolder())) {
            String path = file.getPath().replace("/", "\\");
            if (!path.endsWith(".yml")) {
                return;
            }
            path = path.replace(".yml", "");
            path = MessageUtil.subString(path, "\\menu\\");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            getMenuHashMap().put(path, config);
        }
    }

    /**
     * 获取菜单配置文件实例
     *
     * @param id 菜单ID
     * @return 配置文件实例
     */
    public static YamlConfiguration getMenuConfig(String id) {
        return getMenuHashMap().get(id);
    }
}
