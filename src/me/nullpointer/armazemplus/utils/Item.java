/*
 *
 *   Copyright © 2020 Dev_NullPointer
 *   All rights reserved.
 *
 *   Any redistribution or reproduction of this software, in any form, is prohibited.
 *
 *   -- Discord: NullPointerException#7966 --
 *
 */

package me.nullpointer.armazemplus.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class Item {

    private ItemStack itemStack;
    private Boolean closeable = false;
    private Boolean editable = true;
    private Boolean cancellable = false;
    private Consumer<InventoryClickEvent> clickEventConsumer;

    public Item() {
    }

    public Item(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public Item(ItemStack item) {
        this.itemStack = item;
    }

    public Item(Material m, int amount, short data) {
        this.itemStack = new ItemStack(m, amount, data);
    }

    public Item setType(Material material) {
        if (itemStack == null) itemStack = new ItemStack(material);
        else itemStack.setType(material);
        return this;
    }

    public Boolean isCloseable() {
        return closeable;
    }

    public Boolean isEditable() {
        return editable;
    }

    public Item setCloseable(Boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public Item setCancel(Boolean b) {
        this.cancellable = b;
        return this;
    }

    public Boolean isCancellable() {
        return cancellable;
    }

    public Item setEditable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public Item onClick(Consumer<InventoryClickEvent> consumer) {
        this.clickEventConsumer = consumer;
        return this;
    }

    public Consumer<InventoryClickEvent> getClickEventConsumer() {
        return clickEventConsumer;
    }


    public Item durability(Integer durability) {
        itemStack.setDurability(Short.parseShort(durability.toString()));
        return this;
    }

    public Item name(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);

        return this;
    }

    public Item lore(String... line) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(Arrays.asList(line));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item lore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item amount(Integer amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public Item enchant(Enchantment enchantment, Integer level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public Item hideAttributes() {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item color(Color color) {
        if (!itemStack.getType().name().contains("LEATHER_")) return this;
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(color);
        itemStack.setItemMeta(meta);
        return this;
    }

    public Item glow() {
        ItemMeta im = itemStack.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(im);
        return this;
    }

    public Item unGlow() {
        ItemMeta im = itemStack.getItemMeta();
        im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.removeEnchant(Enchantment.DURABILITY);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemMeta getItemMeta() {
        return itemStack.getItemMeta();
    }

    public Item setItemMeta(ItemMeta im) {
        itemStack.setItemMeta(im);
        return this;
    }

    public Item addLines(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> str = new ArrayList<>(meta.getLore());
        str.addAll(lore);
        meta.setLore(str);
        itemStack.setItemMeta(meta);
        return this;
    }


    public Item setSkullOwner(String owner) {
        if (owner == null || owner.isEmpty()) return this;
        try {
            SkullMeta im = (SkullMeta) itemStack.getItemMeta();
            im.setOwner(owner);
            itemStack.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public Item setSkullUrl(String url) {
        try {
            if (url == null || url.isEmpty()) return this;
            SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException ignored) {
            }
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            } catch (Exception ignored) {
            }
            itemStack.setItemMeta(skullMeta);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}