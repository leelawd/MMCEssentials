package net.moddedminecraft.mmcessentials.commands;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.moddedminecraft.mmcessentials.Config;
import net.moddedminecraft.mmcessentials.MMCEssentials;
import net.moddedminecraft.mmcessentials.utils.Helplist;
import net.moddedminecraft.mmcessentials.utils.Sboard;
import net.moddedminecraft.mmcessentials.utils.Util;

public class RebootCommand  implements CommandExecutor {

	private final MMCEssentials plugin;
	Timer voteTimer;
	Timer nowTimer;
	
	public RebootCommand(MMCEssentials instance) {
		plugin = instance;
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equalsIgnoreCase("reboot")) {			
			if(args.length == 1 && args[0].equalsIgnoreCase("now")) {
				if(sender instanceof Player) {
					if(!sender.hasPermission("mmcessentials.reboot.now")) {
						Util.sendMessage(sender, "&cYou don't have permission to do that!");
						return true;
					}
				}

				plugin.rebootConfirm  = 1;
				Util.sendMessage(sender, "&cPlease type: &6/Reboot Confirm &cif you are sure you want to do this.");

				nowTimer = new Timer();
				nowTimer.schedule(new TimerTask() {
					public void run()
					{
						plugin.rebootConfirm = 0;
						Util.sendMessage(sender, "You took too long to confirm the reboot.");
					}
				}, (60 * 1000));
				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
				if(sender instanceof Player) {
					if(!sender.hasPermission("mmcessentials.reboot.now")) {
						Util.sendMessage(sender, "&cYou don't have permission to do that!");
						return true;
					}
				}
				if (plugin.rebootConfirm == 1) {
					plugin.logToFile("The server was instantly restarted by: " + sender.getName());
					Util.sendMessage(sender, "&cOk, you asked for it!");
					plugin.stopServer();
					return true;
				} else {
					Util.sendMessage(sender, "&cThere is nothing to confirm.");
					return true;
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("time")) {

				if(!Config.autoRestart) {
					Util.sendMessage(sender, "&cThere is no auto-restart scheduled!");
					return true;
				}

				double timeLeft = (Config.restartInterval * 3600) - ((double)(System.currentTimeMillis() - plugin.startTimestamp) / 1000);
				int hours = (int)(timeLeft / 3600);
				int minutes = (int)((timeLeft - hours * 3600) / 60);
				int seconds = (int)timeLeft % 60;

				Util.sendMessage(sender, "&bThe server will be restarting in &f" + hours + "h" + minutes + "m" + seconds + "s");

				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
				showHelp(sender);
				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("on")) {
				if(sender instanceof Player) {
					if(!sender.hasPermission("mmcessentials.reboot.autorestart")) {
						Util.sendMessage(sender, "&cYou don't have permission to do that!");
						return true;
					}
				}

				if(Config.autoRestart) {
					Util.sendMessage(sender, "&cThe server was already automatically restarting!");
					return true;
				}

				Config.autoRestart = true;
				plugin.log.info("[MMCEssentials] scheduling restart tasks...");
				plugin.scheduleTasks();
				plugin.isRestarting = true;

				Util.sendMessage(sender, "&bAutomatic restarts have been turned on!");
				plugin.log.info("[MMCEssentials] " + sender.toString() + " turned automatic restarts on!");
				plugin.logToFile("[MMCEssentials] " + sender.getName() + " turned automatic restarts on!");

				double timeLeft = (Config.restartInterval * 3600) - ((double)(System.currentTimeMillis() - plugin.startTimestamp) / 1000);
				int hours = (int)(timeLeft / 3600);
				int minutes = (int)((timeLeft - hours * 3600) / 60);
				int seconds = (int)timeLeft % 60;

				Util.sendMessage(sender, "&bThe server will be restarting in &f" + hours + "h" + minutes + "m" + seconds + "s");

				return true;
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("off")) {
				if(sender instanceof Player) {
					if(!sender.hasPermission("mmcessentials.reboot.autorestart")) {
						Util.sendMessage(sender, "&cYou don't have permission to do that!");
						return true;
					}
				}

				if(!Config.autoRestart) {
					Util.sendMessage(sender, "&cThe server already wasn't automatically restarting!");
					return true;
				}

				plugin.cancelTasks();

				Util.sendMessage(sender, "&bAutomatic restarts have been turned off!");
				plugin.log.info("[MMCEssentials] " + sender.toString() + " turned automatic restarts off!");
				plugin.logToFile("[MMCEssentials] " + sender.getName() + " turned automatic restarts off!");

				return true;
			}
			else if(args.length >= 2 && (args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("m") || args[0].equalsIgnoreCase("s"))) {
				if(sender instanceof Player) {
					if(!sender.hasPermission("mmcessentials.reboot.schedule")) {
						Util.sendMessage(sender, "&cYou don't have permission to do that!");
						return true;
					}
				}

				if (args.length >= 3) {
					plugin.reason = Util.reason(args, 2, args.length);
				} else {
					plugin.reason = null;
				}

				String timeFormat = args[0];
				double timeAmount = 0;
				try {
					timeAmount = Double.parseDouble(args[1]);
				}
				catch(Exception e) {
					Util.sendMessage(sender, "&cBad time!");
					return true;
				}

				double restartTime = 0;
				if(timeFormat.equalsIgnoreCase("h")) {
					restartTime = timeAmount * 3600;
				}
				else if(timeFormat.equalsIgnoreCase("m")) {
					restartTime = (timeAmount * 60) + 1;
				}
				else if(timeFormat.equalsIgnoreCase("s")) {
					restartTime = timeAmount;
				}
				else {
					Util.sendMessage(sender, "&cInvalid time scale!");
					Util.sendMessage(sender, "&bUse 'h' for time in hours, etc");
					return true;
				}
				
				if (sender instanceof Player) {
					if (!sender.hasPermission("mmcessentials.reboot.limit")) {
						if (restartTime <= 59) {
							Util.sendMessage(sender, "&cYou cannot set the timer lower than 60 seconds.");
							return true;
						} else if (restartTime >= 302) {
							Util.sendMessage(sender, "&cYou cannot set the timer higher than 301 seconds.");
							return true;
						}
					}
				}

				plugin.log.info("[MMCEssentials] " + sender.toString() + " is setting a new restart time...");
				plugin.logToFile("[MMCEssentials] " + sender.getName() + " is setting a new restart time...");
				
				if(Config.autoRestart) {
					plugin.cancelTasks();
				}

				Config.restartInterval = restartTime / 3600.0;

				plugin.log.info("[MMCEssentials] scheduling restart tasks...");
				Sboard.removeScoreboard();
				plugin.scheduleTasks();
				plugin.isRestarting = true;

				if (restartTime <= 300) {
					plugin.displayRestart();
				}

				double timeLeft = (Config.restartInterval * 3600) - ((double)(System.currentTimeMillis() - plugin.startTimestamp) / 1000);
				int hours = (int)(timeLeft / 3600);
				int minutes = (int)((timeLeft - hours * 3600) / 60);
				int seconds = (int)timeLeft % 60;

				if (plugin.reason != null) {
					Util.sendMessage(true, sender, "&bThe server will now be restarting in &f" + hours + "h" + minutes + "m" + seconds + "s, &bwith the reason: \n&d" + plugin.reason);
					//plugin.logToFile(sender.getName() + " scheduled a restart: " + hours + "h " + minutes + "m " + seconds + "s &bfrom now, with the reason: \n&d" + plugin.reason);
				} else {
					Util.sendMessage(true, sender, "&bThe server will now be restarting in &f" + hours + "h" + minutes + "m" + seconds + "s");
					//plugin.logToFile(sender.getName() + " scheduled a restart: " + hours + "h " + minutes + "m " + seconds + "s from now");
				}

				return true;

			} else if(args.length == 1 && args[0].equalsIgnoreCase("vote")) {

				@SuppressWarnings("unused")
				BukkitTask task;
				if (!sender.hasPermission("mmcessentials.reboot.vote.bypass") && Bukkit.getOnlinePlayers().length < Config.minPlayers) {
					Util.sendMessage(sender, ChatColor.RED + "There must be a minimum of 10 players online to start a vote");
				} 
				else
				{
					if (plugin.isRestarting == true) {
						Util.sendMessage(sender, ChatColor.RED + "The server is already restarting!");
					}
					else 
					{
						if (!sender.hasPermission("mmcessentials.reboot.vote.bypass") && plugin.justStarted == true) {
							Util.sendMessage(sender, ChatColor.RED + "The server needs to be online for " + Config.startVoteTimer + " minutes before starting a vote!");
						} 
						else
						{
							if (!sender.hasPermission("mmcessentials.reboot.vote.bypass") && Config.voteAllowed == false) {
								Util.sendMessage(sender, ChatColor.RED + "The server needs to be online for " + Config.startVoteTimer + " minutes before starting a vote!");
							} 
							else
							{
								if (plugin.cdTimer == 1) {
									Util.sendMessage(sender, ChatColor.RED + "You need to wait " + Config.revoteTimer + " minutes before starting another vote!");
								}
								else if (plugin.hasVoted.contains(sender)) {
									Util.sendMessage(sender, ChatColor.RED + "You have already voted!");
								}
								else
								{
									if (!sender.hasPermission("mmcessentials.reboot.vote"))
									{
										Util.sendMessage(sender, ChatColor.RED + "You don't have permission to do this!");
									}
									else
									{
										if (plugin.voteStarted)
										{
											Util.sendMessage(sender, ChatColor.RED + "A vote is already running");
											return true;
										}
										if (sender instanceof Player) {
											Player player = (Player) sender;
											plugin.voteStarted = true;
											plugin.voteCancel = 0;
											plugin.hasVoted.add(player);
											plugin.yesVotes += 1;
											plugin.voteSeconds = 90;
											plugin.displayVotes();
										} else {
											plugin.voteStarted = true;
											plugin.displayVotes();
										}

										plugin.logToFile(sender.getName() + " started a restart vote");

										Util.broadcastMessage(ChatColor.DARK_AQUA + "---------- Restart ----------");
										Util.broadcastMessage(ChatColor.GREEN + sender.getName() + ChatColor.AQUA + " has voted that the server should be restarted");
										Util.broadcastMessage(ChatColor.GOLD + "Type " + ChatColor.GREEN + "/reboot yes" + ChatColor.GOLD + " if you agree");
										Util.broadcastMessage(ChatColor.GOLD + "If you do not agree, Please ignore this.");
										Util.broadcastMessage(ChatColor.GREEN + "" + Util.roundUP(Config.votePercent) + "%" + ChatColor.GOLD + " of the online players need to vote");
										Util.broadcastMessage(ChatColor.GOLD + "yes for the server to restart.");
										Util.broadcastMessage(ChatColor.AQUA + "You have " + ChatColor.GREEN + "90" + ChatColor.AQUA + " seconds to vote!");
										Util.broadcastMessage(ChatColor.DARK_AQUA + "----------------------------");

										//task = new BukkitRunnable()
										//{
										Timer voteTimer = new Timer();
										voteTimer.schedule(new TimerTask() {
											public void run()
											{
												@SuppressWarnings("unused")
												BukkitTask task2;
												double percent = (plugin.yesVotes/ (double)plugin.getServer().getOnlinePlayers().length)*100D;
												if (percent >= Config.votePercent && plugin.voteCancel == 0)
												{
													Util.broadcastMessage("&f[&6Restart&f] &3The server will be restarted in&6 5 minutes &3because enough players have voted for a restart.");
													Sboard.removeScoreboard();
													plugin.logToFile("The restart vote by: " + sender.getName() + " was successful, the server is being restarted.");
													plugin.yesVotes = 0;
													plugin.cdTimer = 1;
													plugin.voteStarted = false;
													plugin.hasVoted.clear();
													plugin.isRestarting = true;
													Config.restartInterval = 300 / 3600.0;
													plugin.log.info("[MMCEssentials] scheduling restart tasks...");
													plugin.usingReason = 1;
													plugin.reason = "Players have voted to restart the server.";
													plugin.scheduleTasks();
												} else {
													if (plugin.voteCancel == 0) {
														Util.broadcastMessage("&f[&6Restart&f] &3The server will not be restarted. Not enough people have voted.");
														plugin.logToFile("The restart vote by: " + sender.getName() + " was not successful.");
														plugin.yesVotes = 0;
														plugin.cdTimer = 1;
														plugin.voteStarted = false;
														plugin.usingReason = 0;
														Sboard.removeScoreboard();
														plugin.hasVoted.clear();
														Timer voteTimer = new Timer();
														voteTimer.schedule(new TimerTask() {
															public void run()
															{
																plugin.cdTimer = 0; 
															}
														}, (long) (Config.revoteTimer * 60000.0));
														//task2 = new BukkitRunnable()
														//{
														//	public void run()
														//	{
														//		plugin.cdTimer = 0;  
														//	}
														//}.runTaskLater(plugin, (long) (Config.revoteTimer * 20 * 60L));
													} else {
														plugin.logToFile("The restart vote by: " + sender.getName() + " was canceled");
														plugin.yesVotes = 0;
														plugin.cdTimer = 1;
														plugin.voteCancel = 0;
														plugin.voteStarted = false;
														plugin.usingReason = 0;
														Sboard.removeScoreboard();
														plugin.hasVoted.clear();
														task2 = new BukkitRunnable()
														{
															public void run()
															{
																plugin.cdTimer = 0;  
															}
														}.runTaskLater(plugin, (long) (Config.revoteTimer * 20 * 60L));
													}
												}
											}
										}, 90000);
										//}.runTaskLater(plugin, 1200L);
									}
								}
							}
						}
					}
				}
				return true;
			} else if(args.length == 2 && args[0].equalsIgnoreCase("vote")) {
				if (args[1].equalsIgnoreCase("on")) {
					Config.voteAllowed = true;
					plugin.getConfig().set("voting.enable", true);
				}
				else if (args[1].equalsIgnoreCase("off")) {
					Config.voteAllowed = false;
					plugin.getConfig().set("voting.enable", false);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("yes")) {
				if (plugin.hasVoted.contains(sender)) {
					Util.sendMessage(sender, "&4You have already voted!");
					return true;
				} else if (plugin.voteStarted == true) {
					plugin.yesVotes += 1;
					if (sender instanceof Player) {
						plugin.hasVoted.add((Player) sender);
					}
					plugin.displayVotes();
					Util.sendMessage(sender, "You Voted Yes!");
					return true;

				} else {
					Util.sendMessage(sender, "&4There is no vote running at the moment");
					return true;
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
				if (sender.hasPermission("mmcessentials.reboot.cancel")) 
				{
					plugin.voteCancel = 1;
					plugin.cancelTasks();
					Sboard.removeScoreboard();
					plugin.isRestarting = false;
					Util.sendMessage(sender, "Restarts have been canceled.");
					return true;
				} else {
					Util.sendMessage(sender, ChatColor.RED + "You do not have permission to use this!");
					return true;
				}
			} else if(args.length >= 1 && args[0].equalsIgnoreCase("debug")) {
				if (sender.hasPermission("mmcessentials.reboot.debug")) {
					if(args.length == 1) {
						sender.sendMessage("Usage: /reboot debug (values/display/remove/delvote/addvote)");
						return true;
					}
					else if(args.length == 2 && args[1].equalsIgnoreCase("values")) {
						sender.sendMessage("autorestart.enabled: " + Config.autoRestart);
						sender.sendMessage("autorestart.interval: " + Config.restartInterval);
						sender.sendMessage("timer.re-vote: " + Config.revoteTimer);
						sender.sendMessage("timer.start-vote: "  + Config.startVoteTimer);
						sender.sendMessage("timer.vote-percent: "  + Config.votePercent);
						sender.sendMessage("timer.minimum-players: "  + Config.minPlayers);
						sender.sendMessage("timer.warning-broadcast: "  + Config.warnTimes);
						sender.sendMessage("isRestarting: "  + plugin.isRestarting);
						sender.sendMessage("voteStarted: "  + plugin.voteStarted);
						return true;
					}
					else if(args.length == 2 && args[1].equalsIgnoreCase("display")) {
						if (plugin.voteStarted = true) {
							plugin.displayVotes();
						} else {
							Util.sendMessage(sender, "&cThere is no vote running right now.");
						}
						return true;
					}
					else if(args.length == 2 && args[1].equalsIgnoreCase("remove")) {
						Sboard.removeScoreboard();
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("delvote")) {
						if (plugin.voteStarted = true) {
							plugin.yesVotes -= 1;
							plugin.displayVotes();
						} else {
							Util.sendMessage(sender, "&cThere is no vote running right now.");
						}
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("addvote")) {
						if (plugin.voteStarted = true) {
							plugin.yesVotes += 1;
							plugin.displayVotes();
						} else {
							Util.sendMessage(sender, "&cThere is no vote running right now.");
						}
						return true;
					}
					else if (args.length == 2 && args[1].equalsIgnoreCase("started")) {
						if (plugin.justStarted = true) {
							plugin.justStarted = false;
							Util.sendMessage(sender, "justStarted: " + plugin.justStarted);
						} else {
							plugin.justStarted = true;
							Util.sendMessage(sender, "justStarted: " + plugin.justStarted);
						}
						return true;
					}
					return true;
				}else{
					sender.sendMessage("&cYou dont have the permission to use this.");
					return true;
				}
			}

		}

		Util.sendMessage(sender, "&cInvalid command usage! Type /Reboot help");
		return true;
	}
	
	void showHelp(CommandSender sender) {
			Util.sendMessage(sender, "&f--- &3Reboot &bHelp &f---");

			ArrayList<Helplist> helpList = new ArrayList<Helplist>();

			helpList.add(new Helplist("&3[] = required  () = optional"));
			helpList.add(new Helplist("&3/reboot &bhelp - &7shows this help"));

			if (sender.hasPermission("mmcessentials.reboot.now")) {
				helpList.add(new Helplist("&3/reboot &bnow - &7restarts the server instantly"));
			} if (sender.hasPermission("mmcessentials.reboot.schedule")) {
				helpList.add(new Helplist("&3/reboot &7[&bh&7|&bm&7|&bs&7] &f[time] (reason) - &7restart the server after a given time"));
			} if (sender.hasPermission("mmcessentials.reboot.cancel")) {	
				helpList.add(new Helplist("&3/reboot &bcancel - &7cancel any current restart timer"));
			} if (sender.hasPermission("mmcessentials.reboot.vote")) {	
				helpList.add(new Helplist("&3/reboot &bvote - &7starts a vote to restart the server"));
			}
			
			helpList.add(new Helplist("&3/reboot &btime - &7informs you how much time is left before restarting"));
			helpList.add(new Helplist("&3/reboot &byes - &7vote yes to restart the server"));

			for(int i = 0; i < helpList.size(); i++) {
				Util.sendMessage(sender, helpList.get(i).command);

			}
		}
}
