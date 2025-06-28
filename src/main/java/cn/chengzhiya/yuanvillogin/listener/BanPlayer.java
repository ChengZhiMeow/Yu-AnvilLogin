package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.util.config.LangUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public final class BanPlayer implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        String banTimeKey = player.getName() + "_banTime";
        String banTimeString = Main.instance.getCacheManager().get(banTimeKey);
        if (banTimeString != null) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, LangUtil.i18n("ban")
                    .replace("{time}", banTimeString)
            );
        }
    }
}
