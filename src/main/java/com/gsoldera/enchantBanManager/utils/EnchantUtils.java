package com.gsoldera.enchantBanManager.utils;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class EnchantUtils {
    
    public void initializeEnchantments() {
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            if (!EnchantBanManager.blockedEnchants.containsKey(enchantment)) {
                EnchantBanManager.blockedEnchants.put(enchantment, false);
            }
        }
    }

    public Enchantment newChosenEnchantment(ItemStack item, long seed) {
        Random rand = new Random();
        rand.setSeed(seed);

        ArrayList<Enchantment> availableEnchants = new ArrayList<>(EnchantBanManager.allowedEnchant);
        
        while (!availableEnchants.isEmpty()) {
            int chosen = rand.nextInt(availableEnchants.size());
            Enchantment enchant = availableEnchants.get(chosen);
            
            if (item.getType() == Material.BOOK || enchant.canEnchantItem(item)) {
                return enchant;
            }
            
            availableEnchants.remove(chosen);
        }
        
        return null;
    }
}