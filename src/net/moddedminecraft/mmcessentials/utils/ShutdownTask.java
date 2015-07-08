package net.moddedminecraft.mmcessentials.utils;

import java.util.TimerTask;

import net.moddedminecraft.mmcessentials.MMCEssentials;

public class ShutdownTask extends TimerTask{

	private final MMCEssentials plugin;

	public ShutdownTask(MMCEssentials instance) {
		plugin = instance;
	}

	@Override
	public void run() {
		plugin.stopServer();

	}

}
