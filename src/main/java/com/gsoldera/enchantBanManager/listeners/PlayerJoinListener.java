package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Inventory inv = p.getInventory();

        for(ItemStack i : inv.getContents()){
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