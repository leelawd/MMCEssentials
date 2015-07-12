package net.moddedminecraft.mmcessentials.commands;

import java.util.ArrayList;

import net.moddedminecraft.mmcessentials.MMCEssentials;
import net.moddedminecraft.mmcessentials.utils.Helplist;
import net.moddedminecraft.mmcessentials.utils.Util;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMCECommand implements CommandExecutor {
	
	private final MMCEssentials plugin;
	
	public MMCECommand(MMCEssentials instance) {
		plugin = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
		
		Player player = null;
		if (sender instanceof Player)  {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("mmce")) {
			if (args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					showHelp(sender);
					return true;
				}
				else if(args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("mmcessentials.reload")) {
						plugin.ReloadCfg();
						Util.sendMessage(sender, "&2Reloaded the config.yml of " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
						return true;
					} else {
						Util.sendMessage(sender, "&4You do not have permission to use this!");
						return true;
					}
				}
			}
		}
		else if (cmd.getName().equalsIgnoreCase("blockinfo")) {
			if (sender.hasPermission("mmcessentials.blockinfo")) {
			ItemStack inHand = player.getItemInHand();
		    player.sendMessage("In Hand: " + String.format("%s(%d:%d)", inHand.getType().name(), inHand.getTypeId(), inHand.getData().getData()));
		    
		    Block inWorld = plugin.getTargetNonAirBlock(player, 300);
		    player.sendMessage("In World: " + String.format("%s(%d:%d)", inWorld.getType().name(), inWorld.getTypeId(), inWorld.getData()));
		    
		    return true;
			}
			else {
				Util.sendMessage(sender, "&4You do not have permission to use this!");
			}
		}
		Util.sendMessage(sender, "&cInvalid command usage! Type /mmce help");
		return true;
	}

	void showHelp(CommandSender sender) {
		Util.sendMessage(sender, "&f--- &3MMCEssentials &bHelp &f---");

		ArrayList<Helplist> helpList = new ArrayList<Helplist>();

		//helpList.add(new Helplist("&3[] = required  () = optional"));
		helpList.add(new Helplist("&3/mmce &bhelp - &7shows this help"));
		if (sender.hasPermission("mmcessentials.reload")) {
			helpList.add(new Helplist("&3/mmce &breload - &7reload the plugin config.yml"));
		}
		if (sender.hasPermission("mmcessentials.blockinfo")) {
			helpList.add(new Helplist("&3/blockinfo - &7In-hand and In-world block info."));
		}

		for(int i = 0; i < helpList.size(); i++) {
			Util.sendMessage(sender, helpList.get(i).command);

		}
	}

}
