package cn.chengzhiya.yuanvillogin.hook.impl;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MythicMobsImpl {
    private final MythicBukkit api;

    public MythicMobsImpl() {
        this.api = MythicBukkit.inst();
    }

    /**
     * 获取指定物品ID的物品实例
     *
     * @param id 物品ID
     * @return 物品实例
     */
    public ItemStack getItem(String id) {
        return getApi().getItemManager().getItemStack(id);
    }

    /**
     * 获取指定物品实例的物品ID
     *
     * @param itemStack 物品实例
     * @return 物品ID
     */
    public String getItemId(ItemStack itemStack) {
        return getApi().getItemManager().getMythicTypeFromItem(itemStack);
    }

    /**
     * 检测指定实体实例是否为MM实体
     *
     * @param entity 实体实例
     * @return 结果
     */
    public boolean isMythicMob(Entity entity) {
        return getApi().getMobManager().isMythicMob(entity);
    }
}
