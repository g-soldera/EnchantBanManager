package com.gsoldera.enchantBanManager;

import com.gsoldera.enchantBanManager.commands.MenuCommand;
import com.gsoldera.enchantBanManager.listeners.*;
import com.gsoldera.enchantBanManager.utils.ConfigUtils;
import com.gsoldera.enchantBanManager.utils.EnchantUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class EnchantBanManager extends JavaPlugin {

    public static HashMap<Enchantment, Boolean> blockedEnchants;
    public static ArrayList<Enchantment> allowedEnchant;
    public static EnchantUtils enchantUtils;

    @Override
    public void onEnable() {
        blockedEnchants = new HashMap<>();
        allowedEnchant = new ArrayList<>();
        enchantUtils = new EnchantUtils();

        saveDefaultConfig();
        
        Bukkit.getServer().getPluginManager().registerEvents(new PrepareItemEnchantListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemClickListener(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EnchantItemListener(), this);

        if(getConfig().getBoolean("PurgeExistingDisabledEnchantedItemsToo")) {
            Bukkit.getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new EntityPickupItemListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerConsumeItemListener(), this);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerHeldItemListener(), this);
        }
        
        enchantUtils.initializeEnchantments();
        ConfigUtils.loadHashMapFromConfig(this);
        
        MenuCommand menuCommand = new MenuCommand(this);
        Objects.requireNonNull(getCommand("ebm")).setExecutor(menuCommand);
        getServer().getPluginManager().registerEvents(new MenuListener(this, menuCommand), this);
        
        allowedEnchant.clear();
        getLogger().info("Loading allowed enchantments...");
        for(Enchantment en : blockedEnchants.keySet()) {
            if(!blockedEnchants.get(en)) {
                allowedEnchant.add(en);
            }
        }
        getLogger().info("Allowed enchantments loaded!");
    }

    @Override
    public void onDisable() {
        try {
            if (blockedEnchants != null && !blockedEnchants.isEmpty()) {
                ConfigUtils.syncHashMapWithConfig(this);
            }
        } catch (Exception e) {
            getLogger().warning("Error saving config on disable: " + e.getMessage());
        }
    }
}