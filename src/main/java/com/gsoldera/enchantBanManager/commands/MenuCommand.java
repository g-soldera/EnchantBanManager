package com.gsoldera.enchantBanManager.commands;

import com.gsoldera.enchantBanManager.EnchantBanManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public class MenuCommand implements CommandExecutor {
    EnchantBanManager plugin;

    public MenuCommand(EnchantBanManager plugin){
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]){
        if(!(sender instanceof Player player)){
            return true;
        }

        if(!player.hasPermission("ebm.admin")){
            return true;
        }

        openEnchantMenu(player, 1);
        return true;
    }

    public void openEnchantMenu(Player player, int page) {
        Inventory inv = Bukkit.getServer().createInventory(null, 45, 
            ChatColor.GRAY + "" + ChatColor.BOLD + "Enchantments - Page " + page);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

        ArrayList<Enchantment> enchants = new ArrayList<>(EnchantBanManager.blockedEnchants.keySet());
        enchants.sort((e1, e2) -> {
            String name1 = e1.getKey().getKey();
            String name2 = e2.getKey().getKey();
            return name1.compareToIgnoreCase(name2);
        });

        int totalEnchants = enchants.size();
        int startIndex = (page - 1) * 27;
        int endIndex = Math.min(startIndex + 27, totalEnchants);

        for (int i = startIndex; i < endIndex; i++) {
            ItemStack book = createEnchantedBook(enchants.get(i));
            inv.addItem(book);
        }

        ItemStack previousPage;
        if (page == 1) {
            previousPage = createNavigationButton("Previous Page", Material.BARRIER);
        } else {
            previousPage = createNavigationButton("Previous Page", Material.ARROW);
        }
        inv.setItem(36, previousPage);

        if (totalEnchants > (page * 27)) {
            ItemStack nextPage = createNavigationButton("Next Page", Material.ARROW);
            inv.setItem(44, nextPage);
        }

        ItemStack help = getStack();
        inv.setItem(40, help);

        player.openInventory(inv);
    }

    private static ItemStack getStack() {
        ItemStack help = new ItemStack(Material.PAPER, 1);
        ItemMeta helpMeta = help.getItemMeta();
        assert helpMeta != null;
        helpMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Enchantment Ban Manager Guide:");
        
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Enchantments that are labeled as Enabled");
        lore.add(ChatColor.GRAY + "are able to be obtained by survival players");
        lore.add(ChatColor.GRAY + "whereas enchantments that are labeled as Disabled");
        lore.add(ChatColor.GRAY + "are not obtainable for survival players");
        helpMeta.setLore(lore);
        help.setItemMeta(helpMeta);
        return help;
    }

    private ItemStack createNavigationButton(String name, Material material) {
        ItemStack button = new ItemStack(material, 1);
        ItemMeta meta = button.getItemMeta();
        assert meta != null;
        
        String cleanName = name.replace("§c§l", "").replace("§a§l", "");
        String displayName;
        
        if (cleanName.contains("Previous")) {
            displayName = ChatColor.RED + "" + ChatColor.BOLD + "← Previous Page";
        } else {
            displayName = ChatColor.GREEN + "" + ChatColor.BOLD + "Next Page →";
        }
        
        meta.setDisplayName(displayName);
        button.setItemMeta(meta);
        return button;
    }

    private ItemStack createEnchantedBook(Enchantment en) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
        assert bookMeta != null;
        bookMeta.setDisplayName(ChatColor.WHITE + "" + en.getKey().toString());

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        if(EnchantBanManager.blockedEnchants.get(en)){
            lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Disabled");
            lore.add(ChatColor.GRAY + "Click to " + ChatColor.GREEN + "Enable");
        } else {
            lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Enabled");
            lore.add(ChatColor.GRAY + "Click to " + ChatColor.RED + "Disable");
        }

        bookMeta.setLore(lore);
        bookMeta.addStoredEnchant(en, en.getMaxLevel(), true);
        book.setItemMeta(bookMeta);
        return book;
    }
}