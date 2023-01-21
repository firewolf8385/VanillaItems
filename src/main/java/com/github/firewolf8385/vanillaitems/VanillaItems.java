package com.github.firewolf8385.vanillaitems;

import com.github.firewolf8385.customitemapi.CustomItemAPI;
import com.github.firewolf8385.customitemapi.addon.Addon;
import com.github.firewolf8385.customitemapi.items.CustomItem;
import com.github.firewolf8385.customitemapi.items.ItemRarity;
import com.github.firewolf8385.customitemapi.items.ItemType;
import com.github.firewolf8385.customitemapi.utils.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class VanillaItems extends JavaPlugin implements Listener {
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        Addon addon = new Addon(this, "vanillaitems", Material.GRASS_BLOCK);

        for(Material material : Material.values()) {
            if(!material.isItem() || material.isAir() || material.isEmpty()) {
                continue;
            }

            ConfigurationSection modifiedItems = settingsManager.getConfig().getConfigurationSection("Items");
            if(modifiedItems.getKeys(false).contains(material.toString())) {
                ConfigurationSection item = modifiedItems.getConfigurationSection(material.toString());

                CustomItem customItem = new CustomItem(material.toString().toLowerCase());
                ItemBuilder builder = new ItemBuilder(material);
                builder.setDisplayName(Component.translatable(material.translationKey()));

                if(item.contains("description")) {
                    customItem.addDescription(item.getStringList("description"));
                }

                customItem.setItem(builder.build());

                if(item.contains("durability")) {
                    customItem.setMaxDurability(item.getInt("durability"));
                }

                if(item.contains("rarity")) {
                    customItem.setRarity(ItemRarity.valueOf(item.getString("rarity")));
                }
                else {
                    customItem.setRarity(ItemRarity.COMMON);
                }

                if(item.contains("type")) {
                    customItem.setType(ItemType.valueOf(item.getString("type")));
                }

                if(item.contains("defense")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("defense"), item.getInt("defense"));
                }

                if(item.contains("toughness")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("toughness"), item.getInt("toughness"));
                }

                if(item.contains("health")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("health"), item.getInt("health"));
                }

                if(item.contains("speed")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("speed"), item.getInt("speed"));
                }

                if(item.contains("luck")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("luck"), item.getInt("luck"));
                }

                if(item.contains("damage")) {
                    customItem.addItemAttribute(CustomItemAPI.getAttributeManager().getAttribute("damage"), item.getInt("damage"));
                }

                addon.registerItem(customItem);
            }
            else {
                CustomItem customItem = new CustomItem(material.toString().toLowerCase());
                customItem.setItem(new ItemBuilder(material).setDisplayName(Component.translatable(material.translationKey())).build());
                customItem.setRarity(ItemRarity.COMMON);
                addon.registerItem(customItem);
            }
        }

        CustomItemAPI.getAddonManager().registerAddon(addon);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        ItemStack itemStack = item.getItemStack();

        if(CustomItemAPI.isCustomItem(itemStack)) {
            return;
        }

        ItemBuilder builder = new ItemBuilder(itemStack);
        builder.setPersistentData("ci-id", itemStack.getType().toString().toLowerCase());
        ItemStack customItem = builder.build();

        item.setItemStack(CustomItemAPI.fromItemStack(customItem).update(customItem));
    }
}