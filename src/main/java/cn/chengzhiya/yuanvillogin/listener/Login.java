package cn.chengzhiya.yuanvillogin.listener;

import cn.chengzhiya.yuanvillogin.util.AnvilUtil;
import fr.xephi.authme.events.LoginEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public final class Login implements Listener {
    @Getter
    private static final List<String> noBlackList = new ArrayList<>();

    @EventHandler
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();

        AnvilUtil.getAnvilInputHashMap().remove(player.getName());
        FakePlayerInventory.getFakePlayerInventoryHashMap().remove(player.getName());
        player.updateInventory();
        player.closeInventory();
    }
}
