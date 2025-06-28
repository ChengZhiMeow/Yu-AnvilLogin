package cn.chengzhiya.yuanvillogin.menu;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.builder.ItemStackBuilder;
import cn.chengzhiya.yuanvillogin.listener.Login;
import cn.chengzhiya.yuanvillogin.text.TextComponent;
import cn.chengzhiya.yuanvillogin.util.AnvilUtil;
import cn.chengzhiya.yuanvillogin.util.PluginUtil;
import cn.chengzhiya.yuanvillogin.util.action.ActionUtil;
import cn.chengzhiya.yuanvillogin.util.config.ConfigUtil;
import cn.chengzhiya.yuanvillogin.util.config.LangUtil;
import cn.chengzhiya.yuanvillogin.util.config.MenuConfigUtil;
import cn.chengzhiya.yuanvillogin.util.menu.MenuUtil;
import cn.chengzhiya.yuanvillogin.util.message.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Getter
public final class LoginMenu extends AbstractMenu {
    private final TextComponent title;

    public LoginMenu(Player player, TextComponent title) {
        super(
                player,
                MenuConfigUtil.getMenuConfig("login.yml")
        );
        this.title = Objects.requireNonNullElseGet(
                title,
                () -> ColorUtil.color(Objects.requireNonNull(getConfig().getString("title")))
        );
    }

    @Override
    public @NotNull Inventory getInventory() {
        Inventory menu;
        if (PluginUtil.isNativeSupportAdventureApi()) {
            menu = Bukkit.createInventory(this, InventoryType.ANVIL, title);
        } else {
            menu = Bukkit.createInventory(this, InventoryType.ANVIL, title.toLegacyString());
        }

        {
            ConfigurationSection items = getConfig().getConfigurationSection("items");
            if (items == null) {
                return menu;
            }

            for (String key : items.getKeys(false)) {
                ConfigurationSection item = items.getConfigurationSection(key);
                if (item == null) {
                    continue;
                }

                ItemStackBuilder itemStackBuilder = MenuUtil.getMenuItemStackBuilder(getPlayer(), item, key)
                        .persistentDataContainer("type", PersistentDataType.STRING, "items");

                String id = item.getString("id");
                if (id != null) {
                    itemStackBuilder.persistentDataContainer("id", PersistentDataType.STRING, id);
                }

                List<Integer> slotList = MenuUtil.getSlotList(item);
                for (Integer slot : slotList) {
                    menu.setItem(slot, itemStackBuilder.build());
                }
            }
        }

        return menu;
    }

    @Override
    public Inventory getPlayerInventory() {
        Inventory menu = Bukkit.createInventory(this, 36);

        ConfigurationSection items = getConfig().getConfigurationSection("playerItems");
        if (items == null) {
            return menu;
        }

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) {
                continue;
            }

            ItemStackBuilder itemStackBuilder = MenuUtil.getMenuItemStackBuilder(getPlayer(), item, key)
                    .persistentDataContainer("type", PersistentDataType.STRING, "playerItems");

            String id = item.getString("id");
            if (id != null) {
                itemStackBuilder.persistentDataContainer("id", PersistentDataType.STRING, id);
            }

            List<Integer> slotList = MenuUtil.getSlotList(item);
            for (Integer slot : slotList) {
                menu.setItem(slot, itemStackBuilder.build());
            }
        }

        return menu;
    }

    @Override
    public void open(InventoryOpenEvent event) {
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("openActions"));
    }

    @Override
    public void click(InventoryClickEvent event) {
        ItemStack item = MenuUtil.getClickItem(event);
        if (item == null) {
            return;
        }

        event.setCancelled(true);
        clickItem(item);
    }

    @Override
    public void clickItem(ItemStack item) {
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        String type = container.get(new NamespacedKey(Main.instance, "type"), PersistentDataType.STRING);
        if (type == null) {
            return;
        }

        String key = container.get(new NamespacedKey(Main.instance, "key"), PersistentDataType.STRING);
        if (key == null) {
            return;
        }

        ConfigurationSection config = getConfig().getConfigurationSection(type);
        if (config == null) {
            return;
        }
        MenuUtil.runItemClickAction(getPlayer(), config, key);

        String id = container.get(new NamespacedKey(Main.instance, "id"), PersistentDataType.STRING);
        if (id == null) {
            return;
        }
        switch (id) {
            case "重置填写" -> new LoginMenu(getPlayer(), LangUtil.i18n("inventory.title.reInput")).openMenu();
            case "登录" -> {
                String password = AnvilUtil.getAnvilInputHashMap().get(getPlayer().getName());
                if (password == null) {
                    new LoginMenu(getPlayer(), LangUtil.i18n("inventory.title.noInput")).openMenu();
                    return;
                }

                if (!Main.instance.getPluginHookManager().getAuthmeHook()
                        .isRegistered(getPlayer())
                ) {
                    new RegisterMenu(getPlayer(), null).openMenu();
                    return;
                }

                String errorsKey = getPlayer().getName() + "_errors";
                if (!Main.instance.getPluginHookManager().getAuthmeHook()
                        .checkPassword(getPlayer(), password)
                ) {
                    ConfigurationSection errorAmount = ConfigUtil.getConfig().getConfigurationSection("loginSettings.errorAmount");
                    if (errorAmount == null || !errorAmount.getBoolean("enable")) {
                        new LoginMenu(getPlayer(), LangUtil.i18n("inventory.title.error")).openMenu();
                        return;
                    }

                    String errorsString = Main.instance.getCacheManager().get(errorsKey);
                    int errors = errorsString != null ? Integer.parseInt(errorsString) : 0;
                    int max = errorAmount.getInt("max");

                    errors++;
                    Main.instance.getCacheManager().put(errorsKey, String.valueOf(errors));
                    if (errors < max) {
                        new LoginMenu(getPlayer(), LangUtil.i18n("inventory.title.errorAmount")
                                .replace("{amount}", String.valueOf(max - errors))
                        ).openMenu();
                        return;
                    }

                    int time = errorAmount.getInt("banTime");

                    TextComponent text = LangUtil.i18n("ban")
                            .replace("{time}", String.valueOf(time));
                    if (PluginUtil.isNativeSupportAdventureApi()) {
                        getPlayer().kick(text);
                    } else {
                        getPlayer().kickPlayer(text.toLegacyString());
                    }

                    String banTimeKey = getPlayer().getName() + "_banTime";
                    Main.instance.getCacheManager().put(banTimeKey, String.valueOf(time));
                    return;
                }

                getPlayer().closeInventory();
                Main.instance.getCacheManager().put(errorsKey, "0");
                Main.instance.getPluginHookManager().getAuthmeHook().loginPlayer(getPlayer());
            }
        }
    }

    @Override
    public void close(InventoryCloseEvent event) {
        if (!Main.instance.getPluginHookManager().getAuthmeHook().isLoggedIn(getPlayer()) &&
                !Login.getNoBlackList().contains(getPlayer().getName())
        ) {
            this.openMenu();
        }
        ActionUtil.runActionList(getPlayer(), getConfig().getStringList("closeActions"));
    }
}
