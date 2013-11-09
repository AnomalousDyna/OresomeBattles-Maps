package com.oresomecraft.BattleMaps.maps;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.oresomecraft.BattleMaps.*;
import com.oresomecraft.OresomeBattles.api.*;

import java.util.ArrayList;
import java.util.List;

public class Zoned extends BattleMap implements IBattleMap, Listener {

    public Zoned() {
        super.initiate(this, name, fullName, creators, modes);
        setAllowBuild(false);
        disableDrops(new Material[]{Material.STONE_SWORD, Material.LEATHER_HELMET});
    }

    String name = "zoned";
    String fullName = "Zoned";
    String creators = "AnomalousRei, MiCkEyMiCE and _Moist";
    Gamemode[] modes = {Gamemode.CTF, Gamemode.INFECTION, Gamemode.KOTH};

    public void readyTDMSpawns() {
        redSpawns.add(new Location(w, -2, 88, 49));
        blueSpawns.add(new Location(w, -2, 88, -26));

        Location blueFlag = new Location(w, -3, 89, -56);
        Location redFlag = new Location(w, -3, 89, 78);
        setCTFFlags(name, redFlag, blueFlag);

        setKoTHMonument(new Location(w, -3, 92, 11));
    }

    public void readyFFASpawns() {
        FFASpawns.add(new Location(w, -2, 87, 88));
        FFASpawns.add(new Location(w, -2, 87, -65));
    }

    public void applyInventory(final BattlePlayer p) {
        Inventory i = p.getInventory();

        ItemStack HEALTH = new ItemStack(Material.GOLDEN_APPLE, 2);
        ItemStack STEAK = new ItemStack(Material.COOKED_BEEF, 3);
        ItemStack JUMP = new ItemStack(Material.FIREWORK, 3);
        ItemStack BOW = new ItemStack(Material.BOW, 1);
        ItemStack SPY_WATCH = new ItemStack(Material.WATCH, 1);
        ItemStack ARROWS = new ItemStack(Material.ARROW, 1);
        ItemStack STONE_SWORD = new ItemStack(Material.STONE_SWORD, 1);

        ItemStack LEATHER_HELMET = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemStack LEATHER_CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        ItemStack LEATHER_PANTS = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        ItemStack LEATHER_BOOTS = new ItemStack(Material.LEATHER_BOOTS, 1);

        ItemMeta spywatchMeta = SPY_WATCH.getItemMeta();
        spywatchMeta.setDisplayName(ChatColor.BLUE + "Spy Watch");
        List<String> spyLore = new ArrayList<String>();
        spyLore.add(org.bukkit.ChatColor.BLUE + "Interact with this watch to go temporarily invisible!");
        spywatchMeta.setLore(spyLore);
        SPY_WATCH.setItemMeta(spywatchMeta);

        InvUtils.colourArmourAccordingToTeam(p, new ItemStack[]{LEATHER_CHESTPLATE, LEATHER_PANTS, LEATHER_HELMET, LEATHER_BOOTS});

        p.getInventory().setBoots(LEATHER_BOOTS);
        p.getInventory().setLeggings(LEATHER_PANTS);
        p.getInventory().setChestplate(LEATHER_CHESTPLATE);
        p.getInventory().setHelmet(LEATHER_HELMET);
        BOW.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        i.setItem(0, STONE_SWORD);
        i.setItem(1, BOW);
        i.setItem(2, STEAK);
        i.setItem(3, HEALTH);
        i.setItem(10, ARROWS);
        i.setItem(4, JUMP);
        i.setItem(8, SPY_WATCH);

    }

    // Region. (Top corner block and bottom corner block.
    // Top left corner.
    public int x1 = 158;
    public int y1 = 139;
    public int z1 = -160;
    //Bottom right corner.
    public int x2 = -171;
    public int y2 = 54;
    public int z2 = 156;

    @EventHandler
    public void onFireworkUse(PlayerInteractEvent event) {
        if (getArena().equals(name)) {
            Player p = event.getPlayer();
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (p.getItemInHand().getType() == Material.FIREWORK) {
                    if (getMode().equals(Gamemode.INFECTION)) {
                        p.sendMessage(ChatColor.RED + "Zoned fireworks on infection act more strangely!");
                        float x = (float) (Math.random() / 2);
                        float z = (float) Math.random() / 2;

                        if (Math.random() > 0.5) x = x - (x * 2);
                        if (Math.random() > 0.5) z = z - (z * 2);
                        p.getInventory().removeItem(new ItemStack(Material.FIREWORK, 1));
                        p.setVelocity(new Vector(x, 1.05, z));
                        return;
                    }
                    p.getInventory().removeItem(new ItemStack(Material.FIREWORK, 1));
                    p.setVelocity(new Vector(0, 1.05, 0));
                }
            }
        }
    }


    @EventHandler
    public void onSpyWatchInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().getWorld().getName().equals(name)) return;
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.WATCH) {
                p.getInventory().remove(p.getItemInHand());
                p.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
                p.getInventory().setChestplate(new ItemStack(Material.AIR, 1));
                p.getInventory().setLeggings(new ItemStack(Material.AIR, 1));
                p.getInventory().setBoots(new ItemStack(Material.AIR, 1));
                p.getInventory().remove(new ItemStack(Material.IRON_BOOTS));
                p.getInventory().remove(new ItemStack(Material.IRON_LEGGINGS));
                p.getInventory().remove(new ItemStack(Material.IRON_HELMET));
                ItemStack LEATHER_CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
                InvUtils.colourArmourAccordingToTeam(BattlePlayer.getBattlePlayer(p), new ItemStack[]{LEATHER_CHESTPLATE});
                p.getInventory().remove(LEATHER_CHESTPLATE);
                p.getInventory().addItem(new ItemStack(Material.IRON_BOOTS));
                p.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS));
                p.getInventory().addItem(new ItemStack(Material.IRON_HELMET));
                p.getInventory().addItem(LEATHER_CHESTPLATE);
                p.updateInventory();
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15 * 20, 0));
            }
        }
    }

}
