package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerHeldItemListener implements Listener {

    @EventHandler
    public void onPlayerHoldItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        for (Enchantment enchant : item.getEnchantments().keySet()) {
            if (!EnchantBanManager.allowedEnchant.contains(enchant)) {
                item.removeEnchantment(enchant);
            }
        }
    }
}
