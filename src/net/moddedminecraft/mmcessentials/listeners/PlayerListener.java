package net.moddedminecraft.mmcessentials.listeners;

import net.moddedminecraft.mmcessentials.MMCEssentials;
import net.moddedminecraft.mmcessentials.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerListener implements Listener {

	private static MMCEssentials plugin;
	
	public PlayerListener(MMCEssentials instance) {
		plugin = instance;
	}

	@SuppressWarnings({ "deprecation", "unused", "static-access" })
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {

		plugin.playersOnline = Bukkit.getServer().getOnlinePlayers().length;

		BukkitTask task;
		
		if (plugin.voteStarted) {
			task = new BukkitRunnable()
			{
				public void run()
				{
					Player player = event.getPlayer();
					plugin.displayVotes();
					Util.sendMessage(player, "&f[&6Restart-Vote&f] &3There is a vote to restart the server.");
					Util.sendMessage(player, ChatColor.GOLD + "Type " + ChatColor.GREEN + "/reboot yes" + ChatColor.GOLD + " if you agree");
					Util.sendMessage(player, ChatColor.GOLD + "If you do not agree, Just keep on playing.");
				}
			}.runTaskLater(plugin, 50L);
		}

	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.playersOnline = Bukkit.getServer().getOnlinePlayers().length;
	}

}
