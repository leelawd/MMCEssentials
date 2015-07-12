package net.moddedminecraft.mmcessentials.utils;

import net.moddedminecraft.mmcessentials.MMCEssentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Sboard {
	
	@SuppressWarnings("unused")
	private static MMCEssentials plugin;

	public Sboard(MMCEssentials instance) {
		plugin = instance;
	}

	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	@SuppressWarnings("deprecation")
	public static void removeScoreboard()
	{
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
	public static ScoreboardManager manager()
	{
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		return manager;
	}
	public static Scoreboard boardSelf()
	{
		Scoreboard board2 = manager().getNewScoreboard();
		return board2;
	}

	public static void unregisterScoreboard() {
		Scoreboard vote = manager().getMainScoreboard();
		if (vote.getObjective(DisplaySlot.SIDEBAR) != null) {
			vote.getObjective(DisplaySlot.SIDEBAR).unregister();
		}
	}
}
