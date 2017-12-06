package com.urakake.discordbot.utils;

import com.urakake.discordbot.commands.Command;
import com.urakake.discordbot.commands.RankCommand;
import com.urakake.discordbot.commands.ReactionRoleAssign;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import java.util.HashMap;


/**
 * Listener for the Bot. Determines if messages or emotes are commands.
 * Adds and removes roles based on commands
 */

public class BotListener extends ListenerAdapter {

    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private ReactionRoleAssign rra;
    private String commandPrefix = "!"; // prefix for commands
    public BotListener(){
        commands.put("rank", new RankCommand());
        rra=new ReactionRoleAssign();
    }

    @Override    // listener for commands
    public void onMessageReceived(MessageReceivedEvent event){
        //System.out.println("MSG Received in "+event.getTextChannel().getName() +" : "+event.getMessage().getContent());
        String msg = event.getMessage().getContent();
        if (event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()){
            if (msg.startsWith(commandPrefix)){
                msg = msg.replaceFirst(commandPrefix,"");
                String[] msgs = msg.split(" ");
                if (!(commands.get(msgs[0])==null) && !commands.get(msgs[0]).action(msgs,event)){
                    System.out.println("Command Failed");
                }
            }
        }
    }

    @Override     // listener for reactions in #rank-select to set
    public void onMessageReactionAdd(MessageReactionAddEvent event){
       if (event.getTextChannel().getName().equals("rank-select")){
            rra.add(event);
        }
    }

    @Override     // listener for reactions in #rank-select to set
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        if (event.getTextChannel().getName().equals("rank-select")){
            rra.remove(event);
        }
    }
}
