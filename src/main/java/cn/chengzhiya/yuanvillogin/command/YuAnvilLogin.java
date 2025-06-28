package cn.chengzhiya.yuanvillogin.command;

import cn.chengzhiya.yuanvillogin.Main;
import cn.chengzhiya.yuanvillogin.menu.LoginMenu;
import cn.chengzhiya.yuanvillogin.menu.RegisterMenu;
import cn.chengzhiya.yuanvillogin.util.action.ActionUtil;
import cn.chengzhiya.yuanvillogin.util.config.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class YuAnvilLogin implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            switch (args[0]) {
                case "about" -> {
                    ActionUtil.sendMessage(sender,
                            LangUtil.i18n("commands.anvillogin.subCommands.about.message")
                                    .replace("{version}", Main.instance.getDescription().getVersion())
                    );
                    return false;
                }
                case "reload" -> {
                    Main.instance.getConfigManager().reloadAll();

                    ActionUtil.sendMessage(sender,
                            LangUtil.i18n("commands.anvillogin.subCommands.reload.message")
                    );
                    return false;
                }
                case "test1" -> {
                    new LoginMenu((Player) sender, null).openMenu();
                    return false;
                }
                case "test2" -> {
                    new RegisterMenu((Player) sender, null).openMenu();
                    return false;
                }
            }
        }
        {
            ActionUtil.sendMessage(sender,
                    LangUtil.i18n("commands.anvillogin.subCommands.help.message")
                            .replace("{helpList}", LangUtil.getHelpList("commands.anvillogin.subCommands"))
                            .replace("{command}", label)
            );
        }
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(LangUtil.getKeys("commands.anvillogin.subCommands"));
        }
        return new ArrayList<>();
    }
}
