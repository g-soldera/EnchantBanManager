package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeItemListener implements Listener {

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent e){
        for(ItemStack i : e.getPlayer().getInventory().getContents()){
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
