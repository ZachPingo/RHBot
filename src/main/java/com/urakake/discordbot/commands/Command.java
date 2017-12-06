package com.urakake.discordbot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Interface for Command classes for the Discord Bot
 */
public interface Command {
    boolean action(String args[], MessageReceivedEvent event);
}
