package cn.chengzhiya.yuanvillogin.manager;

import cn.chengzhiya.yuanvillogin.Main;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

@Getter
public final class AdventureManager {
    private BukkitAudiences adventure;

    public void init() {
        this.adventure = BukkitAudiences.create(Main.instance);
    }

    public void close() {
        if (this.adventure != null) {
            this.adventure.close();
        }

        this.adventure = null;
    }
}
