package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class ItemClickListener implements Listener {
    private final EnchantBanManager plugin;

    public ItemClickListener(EnchantBanManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getClickedInventory().getType().equals(InventoryType.MERCHANT)) {
            Player p = (Player) e.getWhoClicked();
            
            if (e.getCurrentItem() == null) {
                return;
            }

            if (e.getCurrentItem().getType().equals(Material.ENCHANTED_BOOK)) {
                EnchantmentStorageMeta em = (EnchantmentStorageMeta) e.getCurrentItem().getItemMeta();
                for (Enchantment en : em.getStoredEnchants().keySet()) {
                    if (!EnchantBanManager.allowedEnchant.contains(en)) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            for (Enchantment en : e.getCurrentItem().getEnchantments().keySet()) {
                if (!EnchantBanManager.allowedEnchant.contains(en)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}