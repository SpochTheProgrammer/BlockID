package me.spoch.blockid;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main
extends JavaPlugin
implements Listener {
    public String cmdsender;
    public int BlockIDToggle;
    FileConfiguration config;

    public main() {
        this.config = this.getConfig();
    }

    public void onEnable() {
        this.config.addDefault("Message when breaking block", (Object)"&9The ID of the block is");
        this.config.addDefault("Prefix for message when breaking block", (Object)"&e");
        this.config.addDefault("Toggle on message", (Object)"&cShow block ID &ewas toggled on, break a block to get ID");
        this.config.addDefault("Toggle off message", (Object)"&cShow block ID &ewas toggled off");
        this.config.options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.BlockIDToggle = 0;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.cmdsender = sender.toString();
        if (cmd.getName().equalsIgnoreCase("id") && sender.hasPermission("BlockID.toggle")) {
            String ToggleOnMessage = this.config.getString("Toggle on message");
            String ToggleOffMessage = this.config.getString("Toggle off message");
            if (this.BlockIDToggle == 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)ToggleOffMessage));
                this.BlockIDToggle = 0;
            } else if (this.BlockIDToggle == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)ToggleOnMessage));
                this.BlockIDToggle = 1;
            }
        }
        return false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        @SuppressWarnings("deprecation")
		byte data = event.getBlock().getData();
        if (this.BlockIDToggle == 1) {
            String BreakBlockMessage = this.config.getString("Message when breaking block");
            String BreakBlockPrefix = this.config.getString("Prefix for message when breaking block");
            Player player = event.getPlayer();
            String playerevent = player.toString();
            if (playerevent.equals(this.cmdsender)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(String.valueOf(BreakBlockMessage) + " " + BreakBlockPrefix + (Object)event.getBlock().getType() + ":" + data)));
            } else {
                this.BlockIDToggle = 0;
            }
        }
    }
}