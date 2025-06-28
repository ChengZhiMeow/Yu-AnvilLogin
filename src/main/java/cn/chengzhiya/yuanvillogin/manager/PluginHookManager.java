package cn.chengzhiya.yuanvillogin.manager;

import cn.chengzhiya.yuanvillogin.hook.*;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager implements Hook {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final PlaceholderApiHook placeholderAPIHook = new PlaceholderApiHook();
    private final CraftEngineHook craftEngineHook = new CraftEngineHook();
    private final MythicMobsHook mythicMobsHook = new MythicMobsHook();
    private final AuthmeHook authmeHook = new AuthmeHook();

    /**
     * 初始化所有对接的API
     */
    @Override
    public void hook() {
        packetEventsHook.hook();

        placeholderAPIHook.hook();
        craftEngineHook.hook();
        mythicMobsHook.hook();
        authmeHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    @Override
    public void unhook() {
        packetEventsHook.unhook();

        placeholderAPIHook.unhook();
        craftEngineHook.unhook();
        mythicMobsHook.unhook();
        authmeHook.unhook();
    }
}
