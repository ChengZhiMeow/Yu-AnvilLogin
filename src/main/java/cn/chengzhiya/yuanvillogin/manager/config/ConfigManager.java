package cn.chengzhiya.yuanvillogin.manager.config;

import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import cn.chengzhiya.yuanvillogin.util.config.LangUtil;
import cn.chengzhiya.yuanvillogin.util.config.MenuConfigUtil;
import lombok.SneakyThrows;

@SuppressWarnings("unused")
public final class ConfigManager {
    /**
     * 初始化配置文件
     */
    public void init() {
        saveDefaultAll();
        reloadAll();
    }

    /**
     * 保存所有默认配置文件
     */
    @SneakyThrows
    public void saveDefaultAll() {
        ConfigUtil.saveDefaultConfig();
        LangUtil.saveDefaultLang();
        MenuConfigUtil.saveDefaultMenu();
    }

    /**
     * 重载所有配置文件
     */
    public void reloadAll() {
        ConfigUtil.reloadConfig();
        LangUtil.reloadLang();
        MenuConfigUtil.reloadMenu();
    }
}
