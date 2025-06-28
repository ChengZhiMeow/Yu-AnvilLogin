package cn.chengzhiya.yuanvillogin.menu;

import cn.chengzhiya.mhdfscheduler.scheduler.MHDFScheduler;
import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.listener.FakePlayerInventory;
import cn.chengzhiya.yuanvillogin.listener.Login;
import cn.chengzhiya.yuanvillogin.util.reflection.ReflectionUtil;
import fr.xephi.authme.api.v3.AuthMeApi;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class AbstractMenu implements InventoryHolder, Menu {
    private final Player player;
    private final YamlConfiguration config;

    public AbstractMenu(Player player, YamlConfiguration config) {
        this.player = player;
        this.config = config;
    }

    public void onOpen(InventoryOpenEvent event) {
        open(event);
    }

    public void onClick(InventoryClickEvent event) {
        click(event);
    }

    public void onClose(InventoryCloseEvent event) {
        close(event);
    }

    public void clickItem(ItemStack item) {
    }

    public void openMenu() {
        MHDFScheduler.getAsyncScheduler().runTask(Main.instance, () -> {
            Inventory menu = getInventory();

            Login.getNoBlackList().add(getPlayer().getName());
            MHDFScheduler.getGlobalRegionScheduler().runTask(Main.instance, () -> {
                FakePlayerInventory.getFakePlayerInventoryHashMap().put(getPlayer().getName(), getPlayerInventory());

                try {
                    ReflectionUtil.invokeMethod(
                            ReflectionUtil.getMethod(AuthMeApi.class, "openInventory", true, Player.class, Inventory.class),
                            Main.instance.getPluginHookManager().getAuthmeHook().getApi(),
                            player,
                            menu
                    );
                } catch (Exception ignored) {
                    player.openInventory(menu);
                }
                Login.getNoBlackList().remove(getPlayer().getName());
            });
        });
    }
}
