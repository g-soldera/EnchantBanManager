package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class EntityPickupItemListener implements Listener {
    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent e){

        ItemStack i = e.getItem().getItemStack();

        for(Enchantment en : i.getEnchantments().keySet()){
            if(!EnchantBanManager.allowedEnchant.contains(en)){
                i.removeEnchantment(en);
            }
        }
    }
}
