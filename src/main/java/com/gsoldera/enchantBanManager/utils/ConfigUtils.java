package com.gsoldera.enchantBanManager.utils;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtils {

    public static void loadHashMapFromConfig(EnchantBanManager plugin) {
        ConfigurationSection disabledSection = plugin.getConfig().getConfigurationSection("DisabledEnchants");
        
        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            EnchantBanManager.blockedEnchants.put(enchantment, false);
        }
        
        if (disabledSection != null) {
            for (String namespace : disabledSection.getKeys(false)) {
                ConfigurationSection namespaceSection = disabledSection.getConfigurationSection(namespace);
                if (namespaceSection != null) {
                    for (String enchantKey : namespaceSection.getKeys(false)) {
                        if (namespaceSection.getBoolean(enchantKey)) {
                            NamespacedKey key = NamespacedKey.minecraft(enchantKey);
                            Enchantment enchant = Registry.ENCHANTMENT.get(key);
                            if (enchant != null) {
                                EnchantBanManager.blockedEnchants.put(enchant, true);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void syncHashMapWithConfig(EnchantBanManager plugin) {
        if (EnchantBanManager.blockedEnchants == null || EnchantBanManager.blockedEnchants.isEmpty()) {
            return;
        }
        
        Map<String, Map<String, Boolean>> groupedEnchants = new HashMap<>();
        
        for (Map.Entry<Enchantment, Boolean> entry : EnchantBanManager.blockedEnchants.entrySet()) {
            if (entry.getValue()) {
                NamespacedKey key = entry.getKey().getKey();
                String namespace = key.getNamespace();
                String enchantKey = key.getKey();
                
                groupedEnchants
                    .computeIfAbsent(namespace, k -> new HashMap<>())
                    .put(enchantKey, true);
            }
        }
        
        plugin.getConfig().set("DisabledEnchants", null);
        
        if (!groupedEnchants.isEmpty()) {
            for (Map.Entry<String, Map<String, Boolean>> namespaceEntry : groupedEnchants.entrySet()) {
                String path = "DisabledEnchants." + namespaceEntry.getKey();
                for (Map.Entry<String, Boolean> enchantEntry : namespaceEntry.getValue().entrySet()) {
                    plugin.getConfig().set(path + "." + enchantEntry.getKey(), enchantEntry.getValue());
                }
            }
            plugin.saveConfig();
        }
    }
}