package com.urakake.discordbot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.List;

public class ReactionRoleAssign {
    private boolean rankChange = false; // can a player set their own rank

    private String[] rankRoles = {
            "Bronze 1","Bronze 2","Bronze 3",
            "Silver 1","Silver 2","Silver 3",
            "Gold 1","Gold 2","Gold 3",
            "Platinum 1","Platinum 2","Platinum 3",
            "Diamond 1","Diamond 2","Diamond 3",
            "Champion 1","Champion 2","Champion 3","Grand Champion"
    }; // rank roles players can assign themselves (max 4?)
    private String[] roleAssignNames = {
            "NA","EU","OC","AS","AF","SA",
            "STEAM", "XBOX","PS4"};                         // other roles players can assign themselves
    public void add( MessageReactionAddEvent event){
        String reactionName = event.getReactionEmote().getName();
        Guild guild = event.getGuild();
        List<Role> roleList = guild.getRolesByName(reactionName,true);
        if (roleList.size()==1){
            String roleName =roleList.get(0).getName();
            boolean roleMatch = false;
            for (int i=0; i<roleAssignNames.length; i++){
                if (roleAssignNames[i].equalsIgnoreCase(roleName))
                    roleMatch=true;
            }
            if (roleMatch){
                GuildController controller = event.getGuild().getController();
                controller.addSingleRoleToMember(event.getMember(),roleList.get(0)).complete();
            } else if (rankChange) {
                for (int i = 0; i < rankRoles.length; i++) {
                    if (rankRoles[i].equalsIgnoreCase(roleName))
                        roleMatch = true;
                }
                if (roleMatch) {
                    GuildController controller = event.getGuild().getController();
                    controller.addSingleRoleToMember(event.getMember(), roleList.get(0)).complete();
                }
            }
        }
    }
    public void remove( MessageReactionRemoveEvent event){
        String reactionName = event.getReactionEmote().getName();
        Guild guild = event.getGuild();
        List<Role> roleList = guild.getRolesByName(reactionName,true);
        if (roleList.size()==1){
            String roleName =roleList.get(0).getName();
            boolean roleMatch = false;
            for (int i=0; i<roleAssignNames.length; i++){
                if (roleAssignNames[i].equalsIgnoreCase(roleName))
                    roleMatch=true;
            }
            if (roleMatch){
                GuildController controller = event.getGuild().getController();
                controller.removeSingleRoleFromMember(event.getMember(),roleList.get(0)).complete();
            } else if (rankChange){
                for (int i = 0; i < rankRoles.length; i++) {
                    if (rankRoles[i].equalsIgnoreCase(roleName))
                        roleMatch = true;
                }
                if (roleMatch) {
                    GuildController controller = event.getGuild().getController();
                    controller.removeSingleRoleFromMember(event.getMember(), roleList.get(0)).complete();
                }
            }
        }
    }
}
