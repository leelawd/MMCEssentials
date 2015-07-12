package net.moddedminecraft.mmcessentials.listeners;

import net.moddedminecraft.mmcessentials.Config;
import net.moddedminecraft.mmcessentials.MMCEssentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
	
	private static MMCEssentials plugin;

	public CommandListener(MMCEssentials instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		boolean plugins = event.getMessage().startsWith("/plugins");
		boolean pl = event.getMessage().equalsIgnoreCase("/pl");
		boolean pl2 = event.getMessage().startsWith("/pl ");
		boolean plugman = event.getMessage().startsWith("/plugman list");
		boolean gc = event.getMessage().equalsIgnoreCase("/gc");
		boolean icanhasbukkit = event.getMessage().startsWith("/icanhasbukkit");
		boolean unknown = event.getMessage().startsWith("/?");
		boolean version = event.getMessage().startsWith("/version");
		boolean ver = event.getMessage().startsWith("/ver");
		boolean bukkitplugin = event.getMessage().startsWith("/bukkit:plugins");
		boolean bukkitpl = event.getMessage().startsWith("/bukkit:pl");
		boolean bukkitunknown = event.getMessage().startsWith("/bukkit:?");
		boolean about = event.getMessage().startsWith("/about");
		boolean a = event.getMessage().equalsIgnoreCase("/a");
		boolean bukkitabout = event.getMessage().startsWith("/bukkit:about");
		boolean bukkita = event.getMessage().startsWith("/bukkit:a");
		boolean bukkitversion = event.getMessage().startsWith("/bukkit:version");
		boolean bukkitver = event.getMessage().startsWith("/bukkit:ver");
		boolean bukkithelp = event.getMessage().startsWith("/bukkit:help");


		Player player = event.getPlayer();

		if (Config.hideplugins = true) {
			if ((plugins) || (pl) || (pl2) || (plugman) || (bukkitunknown) ||  (unknown) ||  (bukkitplugin) ||  (bukkitpl)) {
				if(!player.hasPermission("mmcessentials.hideplugins.bypass")){
					event.setCancelled(true);
					String defaultMessage = "§a";
					for (String plugin : plugin.plugins) {
						defaultMessage = defaultMessage + plugin + ", ";
					}
					defaultMessage = defaultMessage.substring(0, defaultMessage.lastIndexOf(", "));
					player.sendMessage(ChatColor.WHITE + "Plugins (" + plugin.plugins.size() + "): " + ChatColor.GREEN + defaultMessage.replaceAll(", ", new StringBuilder().append(ChatColor.WHITE).append(", ").append(ChatColor.GREEN).toString()));
				}
			}


			if ((version) || (ver) ||  (gc) ||  (icanhasbukkit) ||  (a) ||  (about) ||  (bukkitversion) ||  (bukkitver)||  (bukkitabout)  ||  (bukkita) ||  (bukkithelp)) {
				if(!player.hasPermission("mmcessentials.hideplugins.bypass")){
					Player p = event.getPlayer();
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You do not have the permission to use that!");
				}
			}
		}

		boolean cofh = event.getMessage().startsWith("/cofh countblocks");
		boolean cofh2 = event.getMessage().startsWith("/cofh enchant");
		boolean cofh3 = event.getMessage().startsWith("/cofh version");
		boolean cofh4 = event.getMessage().startsWith("/cofh clearblocks");
		boolean cofh5 = event.getMessage().startsWith("/cofh killall");
		boolean cofh6 = event.getMessage().startsWith("/cofh unloadchunk");
		boolean cofh7 = event.getMessage().startsWith("/cofh reloadworldgen");
		boolean cofh8 = event.getMessage().startsWith("/cofh tpx");
		boolean cofh9 = event.getMessage().startsWith("/cofh syntax");
		boolean cofh10 = event.getMessage().startsWith("/cofh tps");
		boolean cofh11 = event.getMessage().startsWith("/cofh help");
		boolean cofh12 = event.getMessage().startsWith("/cofh replaceblocks");
		boolean cofh13 = event.getMessage().startsWith("/cofh friend");

		if ((cofh) || (cofh2) || (cofh3) || (cofh4) || (cofh5) || (cofh6) || (cofh7) || (cofh8) || (cofh9) || (cofh10) || (cofh11) || (cofh12)) {
			if (!player.hasPermission("cofh.staff")) {
				Player p = event.getPlayer();
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You do not have the permission to use that!");
			}	
		}

		if ((cofh13)) {
			if (!player.hasPermission("cofh.friend")) {
				Player p = event.getPlayer();
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You do not have the permission to use that!");
			}	
		}
		
		
		boolean td = event.getMessage().equalsIgnoreCase("/tickdynamic tps");
		boolean td2 = event.getMessage().startsWith("/tickdynamic list");
		boolean td3 = event.getMessage().startsWith("/tickdynamic value");
		boolean td4 = event.getMessage().equalsIgnoreCase("/tickdynamic reload");
		boolean td5 = event.getMessage().startsWith("/tickdynamic enabled");
		boolean td6 = event.getMessage().startsWith("/tickdynamic help");


		if ((td3) || (td4) || (td5) || (td6)) {
			if (!player.hasPermission("tickdynamic.staff")) {
				Player p = event.getPlayer();
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You do not have the permission to use that!");
			}	
		}

		if ((td) ||(td2)) {
			if (!player.hasPermission("tickdynamic.tps")) {
				Player p = event.getPlayer();
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You do not have the permission to use that!");
			}	
		}



	}	

}
