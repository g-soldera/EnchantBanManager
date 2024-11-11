package com.gsoldera.enchantBanManager.listeners;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import com.gsoldera.enchantBanManager.commands.MenuCommand;
import com.gsoldera.enchantBanManager.utils.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuListener implements Listener {
    private final EnchantBanManager plugin;
    private final MenuCommand menuCommand;

    public MenuListener(EnchantBanManager plugin, MenuCommand menuCommand) {
        this.plugin = plugin;
        this.menuCommand = menuCommand;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = event.getView().getTitle();
        if (!title.contains("Enchantments")) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null) return;

        if (title.contains("Page")) {
            String titleStr = title.split("Page ")[1];
            int currentPage = Integer.parseInt(titleStr);
            
            if (event.getSlot() == 36 && item.getType() == Material.ARROW) {
                menuCommand.openEnchantMenu(player, currentPage - 1);
                return;
            } else if (event.getSlot() == 44 && item.getType() == Material.ARROW) {
                menuCommand.openEnchantMenu(player, currentPage + 1);
                return;
            }
        }

        if (item.getType() == Material.ENCHANTED_BOOK) {
            handleEnchantmentToggle(player, item);
        }
    }

    private void handleEnchantmentToggle(Player player, ItemStack item) {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) item.getItemMeta();
        if (itemMeta == null) return;

        List<String> lore = itemMeta.getLore();
        if (lore == null || lore.size() < 3) return;

        Enchantment enchant = (Enchantment) itemMeta.getStoredEnchants().keySet().toArray()[0];

        ArrayList<String> newLore = new ArrayList<>();
        newLore.add("");

        if (lore.get(1).contains("Enabled")) {
            newLore.add(ChatColor.RED + "" + ChatColor.BOLD + "Disabled");
            newLore.add(ChatColor.GRAY + "Click to " + ChatColor.GREEN + "Enable");
            EnchantBanManager.blockedEnchants.put(enchant, true);
            EnchantBanManager.allowedEnchant.remove(enchant);
        } else {
            newLore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Enabled");
            newLore.add(ChatColor.GRAY + "Click to " + ChatColor.RED + "Disable");
            EnchantBanManager.blockedEnchants.put(enchant, false);
            EnchantBanManager.allowedEnchant.add(enchant);
        }

        itemMeta.setLore(newLore);
        item.setItemMeta(itemMeta);

        player.updateInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        ConfigUtils.syncHashMapWithConfig(plugin);
    }
}