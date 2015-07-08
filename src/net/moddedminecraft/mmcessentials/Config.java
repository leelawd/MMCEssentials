package net.moddedminecraft.mmcessentials;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	@SuppressWarnings("unused")
	private static MMCEssentials plugin;

	public Config(MMCEssentials instance) {
		plugin = instance;
	}

	static File configFile;

	private static final int confVersion = 1; // Tracking config version

	public static boolean autoRestart = true;
	public static double restartInterval = 6;
	public static List<Double> warnTimes;
	public static int revoteTimer = 10;
	public static int startVoteTimer = 60;
	public static int minPlayers = 10;
	public static int votePercent = 60;
	public static int configVersion = 0;
	public static boolean hideplugins = true;
	public static boolean voteAllowed = true;

	static void loadConfiguration(MMCEssentials plugin) {

		final FileConfiguration config = plugin.getConfig();
		config.options().copyDefaults(true);
		final int ver = config.getInt("version", 0);
		
		if (!plugin.plugins.isEmpty()) {
			plugin.plugins.clear();
		}

		if (ver != Config.confVersion) {
			plugin.getLogger().info("Attempting to update your configuration. Check to make sure it's ok");
			if (ver < 1) {
				config.set("autorestart.enable", false);
				config.set("autorestart.interval", 6);
				config.set("timer.warning-broadcast", null);
				config.set("timer.re-vote", 10);
				config.set("timer.start-vote", 60);
				config.set("timer.vote-percent", 60);
				config.set("timer.minimum-players", 10);
				config.set("voting.enable", true);
				config.set("hideplugins.enabled", true);
				config.set("hideplugins.list", "There, is, nothing, to, see, here");
				config.set("version", Config.confVersion);
			}
		}

		Config.autoRestart = plugin.getConfig().getBoolean("autorestart.enable", false);
		Config.restartInterval = plugin.getConfig().getDouble("autorestart.interval", 6);
		Config.warnTimes = plugin.getConfig().getDoubleList("timer.warning-broadcast");
		Config.revoteTimer = plugin.getConfig().getInt("timer.re-vote", 10);
		Config.startVoteTimer = plugin.getConfig().getInt("timer.start-vote", 60);
		Config.votePercent = plugin.getConfig().getInt("timer.vote-percent", 60);
		Config.minPlayers = plugin.getConfig().getInt("timer.minimum-players", 10);
		Config.voteAllowed = plugin.getConfig().getBoolean("voting.enable", true);
		Config.configVersion = plugin.getConfig().getInt("version", 0);

		Config.hideplugins = plugin.getConfig().getBoolean("hideplugins.enabled", true);

		for (String s : plugin.getConfig().getString("hideplugins.list").split(", ")) {
			plugin.plugins.add(s);
		}
	}

}
