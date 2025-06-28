package cn.chengzhiya.yuanvillogin.entity;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public final class InventoryData {
    private int slot;
    private ItemStack itemStack;
}
