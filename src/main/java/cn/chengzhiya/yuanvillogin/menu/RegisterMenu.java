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
public final class RegisterMenu extends AbstractMenu {
    private final TextComponent title;

    public RegisterMenu(Player player, TextComponent title) {
        super(
                player,
                MenuConfigUtil.getMenuConfig("register")
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

                MenuUtil.setMenuItem(getPlayer(), menu, item, key);
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
            case "重置填写" -> new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.reInput")).openMenu();
            case "注册" -> {
                String password = AnvilUtil.getAnvilInputHashMap().get(getPlayer().getName());
                if (password == null) {
                    new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.noInput")).openMenu();
                    return;
                }

                if (Main.instance.getPluginHookManager().getAuthmeHook()
                        .isRegistered(getPlayer())
                ) {
                    new LoginMenu(getPlayer(), null).openMenu();
                    return;
                }

                ConfigurationSection passwordConfig = ConfigUtil.getConfig().getConfigurationSection("loginSettings.password");
                if (passwordConfig == null) {
                    return;
                }

                if (passwordConfig.getStringList("easy").contains(password)) {
                    new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.easy")).openMenu();
                    return;
                }

                if (password.length() < passwordConfig.getInt("min")) {
                    new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.min")
                            .replace("{amount}", String.valueOf(passwordConfig.getInt("min")))
                    ).openMenu();
                    return;
                }

                if (password.length() > passwordConfig.getInt("max")) {
                    new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.max")
                            .replace("{amount}", String.valueOf(passwordConfig.getInt("max")))
                    ).openMenu();
                    return;
                }

                {
                    ConfigurationSection limitConfig = ConfigUtil.getConfig().getConfigurationSection("loginSettings.limit");
                    if (limitConfig != null) {
                        if (limitConfig.getBoolean("enable")) {
                            List<String> registerPlayerList = Main.instance.getPluginHookManager().getAuthmeHook().getRegisterPlayerList(getPlayer());
                            if (registerPlayerList.size() >= limitConfig.getInt("amount")) {
                                new RegisterMenu(getPlayer(), LangUtil.i18n("inventory.title.maxRegister")
                                        .replace("{amount}", String.valueOf(limitConfig.getInt("amount")))
                                ).openMenu();
                                return;
                            }
                        }
                    }

                }

                getPlayer().closeInventory();
                Main.instance.getPluginHookManager().getAuthmeHook().registerPlayer(getPlayer(), password);
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
