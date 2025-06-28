package cn.chengzhiya.yuanvillogin.manager.config;

import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import cn.chengzhiya.yuanvillogin.util.config.FileUtil;
import lombok.SneakyThrows;

public final class ConfigFolderManager {
    /**
     * 初始化配置文件文件夹
     */
    @SneakyThrows
    public void init() {
        FileUtil.createFolder(ConfigUtil.getDataFolder());
    }
}
