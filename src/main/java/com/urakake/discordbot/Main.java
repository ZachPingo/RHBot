package com.urakake.discordbot;


import com.urakake.discordbot.utils.BotListener;
import com.urakake.discordbot.utils.Hidden;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class Main {

	private static JDA jda;

	/**
	 *  Builds Discord Bot and sets up a listener (BotListener) for commands
	 * @param args args, like what a pirate would say
	 */
	public static void main (String args[]) {
		try {
			jda = new JDABuilder(AccountType.BOT).addEventListener(new BotListener()).setToken(Hidden.botToken).buildBlocking();
			jda.setAutoReconnect(true);
		} catch (IllegalArgumentException e) {
			System.out.println("ERROR: No token entered, fix it.");
		} catch (LoginException e) {
			System.out.println("ERROR: The token entered is invalid, fix it.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
