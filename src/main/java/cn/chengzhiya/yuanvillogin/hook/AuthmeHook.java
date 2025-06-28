package cn.chengzhiya.yuanvillogin.hook;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.util.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public final class AuthmeHook extends AbstractHook {
    private AuthMeApi api;

    @Override
    public void hook() {
        api = AuthMeApi.getInstance();
        super.enable = true;
    }

    @Override
    public void unhook() {
        api = null;
        super.enable = false;
    }

    public AuthMeApi getApi() {
        if (api == null) {
            api = AuthMeApi.getInstance();
        }
        return api;
    }

    /**
     * 检测指定玩家ID密码是否为指定密码
     *
     * @param name     玩家iD
     * @param password 密码
     * @return 结果
     */
    public boolean checkPassword(String name, String password) {
        return getApi().checkPassword(name, password);
    }

    /**
     * 检测指定玩家实例密码是否为指定密码
     *
     * @param player   玩家实例
     * @param password 密码
     * @return 结果
     */
    public boolean checkPassword(OfflinePlayer player, String password) {
        return checkPassword(player.getName(), password);
    }

    /**
     * 检测指定玩家ID是否已经注册
     *
     * @param name 玩家ID
     * @return 结果
     */
    public boolean isRegistered(String name) {
        return getApi().isRegistered(name);
    }

    /**
     * 检测指定玩家实例是否已经注册
     *
     * @param player 玩家实例
     * @return 结果
     */
    public boolean isRegistered(OfflinePlayer player) {
        return isRegistered(player.getName());
    }

    /**
     * 检测指定玩家实例是否已经登录完成
     *
     * @param player 玩家实例
     * @return 结果
     */
    public boolean isLoggedIn(Player player) {
        return getApi().isAuthenticated(player);
    }

    /**
     * 注册指定玩家ID为指定密码
     *
     * @param name     玩家ID
     * @param password 密码
     */
    public void registerPlayer(String name, String password) {
        getApi().registerPlayer(name, password);
    }

    /**
     * 注册指定玩家实例为指定密码
     *
     * @param player   玩家实例
     * @param password 密码
     */
    public void registerPlayer(OfflinePlayer player, String password) {
        registerPlayer(player.getName(), password);
    }

    /**
     * 获取指定IP的所有注册用户列表
     *
     * @param ip IP
     * @return 注册用户列表
     */
    public List<String> getRegisterPlayerList(String ip) {
        return getApi().getNamesByIp(ip);
    }

    /**
     * 获取指定玩家实例同IP的所有注册用户列表
     *
     * @param player 玩家实例
     * @return 注册用户列表
     */
    public List<String> getRegisterPlayerList(Player player) {
        return getRegisterPlayerList(PlayerUtils.getPlayerIp(player));
    }

    /**
     * 登录指定玩家实例
     *
     * @param player 玩家实例
     */
    public void loginPlayer(Player player) {
        getApi().forceLogin(player);
    }
}
