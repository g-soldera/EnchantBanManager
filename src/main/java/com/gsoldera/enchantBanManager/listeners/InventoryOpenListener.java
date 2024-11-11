package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

public class InventoryOpenListener implements Listener {
    @EventHandler
    public void onPlayerOpenAnInventory(InventoryOpenEvent e){

        if(e.getView().getTitle().equals(ChatColor.GRAY + "" + ChatColor.BOLD + "Enchantments")){
            return;
        }

        for(ItemStack i : e.getInventory().getContents()){
            if(i==null || i.getType().equals(Material.AIR)){
                continue;
            }
            for(Enchantment en : i.getEnchantments().keySet()){
                if(!EnchantBanManager.allowedEnchant.contains(en)){
                    i.removeEnchantment(en);
                }
            }
        }
    }
}
