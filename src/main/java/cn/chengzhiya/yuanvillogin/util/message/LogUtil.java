package cn.chengzhiya.yuanvillogin.util.message;

import cn.chengzhiya.yuanvillogin.util.action.ActionUtil;
import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import org.bukkit.Bukkit;

public final class LogUtil {
    private static final String CONSOLE_PREFIX = "[Yu-AnvilLogin] ";
    private static final String DEBUG_PREFIX = "[Yu-AnvilLogin-Debug] ";

    /**
     * 日志消息
     *
     * @param message 文本
     * @param args    参数
     */
    public static void log(String message, String... args) {
        ActionUtil.sendMessage(Bukkit.getConsoleSender(), ColorUtil.color(CONSOLE_PREFIX + MessageUtil.formatString(message, args)));
    }

    /**
     * 调试消息
     *
     * @param message 文本实例
     * @param args    参数
     */
    public static void debug(String message, String... args) {
        if (!ConfigUtil.getConfig().getBoolean("debug")) {
            return;
        }

        ActionUtil.sendMessage(Bukkit.getConsoleSender(), ColorUtil.color(DEBUG_PREFIX + MessageUtil.formatString(message, args)));
    }
}
