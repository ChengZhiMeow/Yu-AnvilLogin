package cn.chengzhiya.yuanvillogin.manager.config;

import cn.chengzhiya.yuanvillogin.util.config.ProxyUtil;
import lombok.SneakyThrows;

public final class ProxyConfigManager {
    /**
     * 初始化代理配置文件
     */
    @SneakyThrows
    public void init() {
        ProxyUtil.saveDefaultProxy();
        ProxyUtil.reloadProxy();
    }
}
