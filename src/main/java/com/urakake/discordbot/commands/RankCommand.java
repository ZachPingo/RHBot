package com.urakake.discordbot.commands;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *  Command (!rank platform username) bot will search stats website and return stats
 */

public class RankCommand implements Command {
    public String platform;
    public String username;
    private String[] rankNames = {
            "Bronze I","Bronze II","Bronze III",
            "Silver I","Silver II","Silver III",
            "Gold I","Gold II","Gold III",
            "Platinum I","Platinum II","Platinum III",
            "Diamond I","Diamond II","Diamond III",
            "Champion I","Champion II","Champion III","Grand Champion"
    };

    private String[] rankRoles = {
            "Bronze 1","Bronze 2","Bronze 3",
            "Silver 1","Silver 2","Silver 3",
            "Gold 1","Gold 2","Gold 3",
            "Platinum 1","Platinum 2","Platinum 3",
            "Diamond 1","Diamond 2","Diamond 3",
            "Champion 1","Champion 2","Champion 3","Grand Champion"
    };

    private String[] emojis = {
            "Bronze_1","Bronze_2","Bronze_3",
            "Silver_1","Silver_2","Silver_3",
            "Gold_1","Gold_2","Gold_3",
            "Platinum_1","Platinum_2","Platinum_3",
            "Diamond_1","Diamond_2","Diamond_3",
            "Champion_1","Champion_2","Champion_3","Grand_Champion"
    };
    /**
     * bot will lookup and say the rank of the player.
     * @param   args    command, platform and username (!rank steam kuxir97) 3 Strings
     * @param   event   the MessageReceivedEvent that called the command
     * using http://kyuu.moe/extra/rankapi.php?user=USERNAME&plat=PLATFORM
     * Simply change USERNAME into your Steam profiles id, psn name, or xbox name,
     * and PLATFORM into either steam, ps or xbox. Use the hyphen character for spaces.
     * returned string looks like
     * kuxir97's current ranks | 1v1: Champion III (1371) | 2v2: Grand Champion (1719) | Solo 3v3: Grand Champion (1350) | 3v3: Grand Champion (1630)
     */
    public boolean action(String args[], MessageReceivedEvent event){
        if (event.getTextChannel().getName().equals("rank-set")){
            if (args.length==3){
                platform = args[1];
                username = args[2];
                if (platform.equalsIgnoreCase("pc")|| platform.equalsIgnoreCase("steam")){
                    platform="steam";
                } else if (platform.equalsIgnoreCase("ps4") || platform.equalsIgnoreCase("ps") || platform.equalsIgnoreCase("playstation")){
                    platform="ps";
                } else if (platform.equalsIgnoreCase("xbox")|| platform.equalsIgnoreCase("xbox1") || platform.equalsIgnoreCase("xboxone")){
                    platform="xbox";
                }
                String rlTrackerAddress = "http://kyuu.moe/extra/rankapi.php?user="+username+"&plat="+platform;
                try {
                    URL url = new URL(rlTrackerAddress);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine, rank ="";
                    while ((inputLine = in.readLine()) != null){
                        rank+=inputLine+"\n";
                    }
                    setRankRoles(rank,event);
                    in.close();
                    String msg = rank;
                    for (int i=rankNames.length-1; i>=0; i--){
                        List<Emote> emo = event.getGuild().getEmotesByName(emojis[i],true);
                        if(emo.size()>0) {
                            msg = msg.replace(rankNames[i],  "<:" + emo.get(0).getName() + ":"+emo.get(0).getIdLong()+">");
                        }
                    }
                    msg = msg.replaceAll("\\|","\n");
                    System.out.println(msg);
                    event.getTextChannel().sendMessage(msg).complete();
                    return true;
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                event.getTextChannel().sendMessage("Usage: !rank platform user").complete();
            }
        }
        return false;
    }
    private void setRankRoles(String rankString, MessageReceivedEvent event){
        int highestRankPlayer=0;
        int highestStringRank=0;
        List<Role> roles = event.getMember().getRoles();
        ArrayList<Integer> allPlayerRanks = new ArrayList<Integer>();

        //Find highest Rank Role the player has
        for (int i=0; i<roles.size();i++){
            for (int j=0;j<rankRoles.length;j++){
                if (roles.get(i).getName().equalsIgnoreCase(rankRoles[j])){
                    if (highestRankPlayer<j) {
                        highestRankPlayer = j;
                    }
                    allPlayerRanks.add(new Integer(j));
                }
            }
        }
        //Find the highest Rank Name in the rank String
        rankString=rankString.replace(" 1v1: ", "");
        rankString=rankString.replace(" 2v2: ", "");
        rankString=rankString.replace(" Solo 3v3: ", "");
        rankString=rankString.replace(" 3v3: ", "");
        rankString=rankString.replaceAll("\\s*\\([^\\)]*\\)\\s*", " ");//remove (MMR) and whitespace
        String[] rankCategories = rankString.split("\\|");
        for (int i=1;i<rankCategories.length;i++){
            String[] rankWords = rankCategories[i].split(" ");
            for (int j=0;j<rankNames.length;j++){
                if(rankWords.length==2) {
                    String firstTwoWords = rankWords[0] + " " + rankWords[1];
                    if (firstTwoWords.equalsIgnoreCase(rankNames[j])) {
                        if (highestStringRank < j) {
                            highestStringRank = j;
                        }
                    }
                }
            }
        }
        if (highestStringRank>0) {
            GuildController controller = event.getGuild().getController();
            for (int i=0;i<allPlayerRanks.size();i++){
                int rankNum = allPlayerRanks.get(i);
                List<Role> roleList = event.getGuild().getRolesByName(rankRoles[rankNum],true);
                controller.removeSingleRoleFromMember(event.getMember(), roleList.get(0)).complete();
            }
            List<Role> roleList = event.getGuild().getRolesByName(rankRoles[highestStringRank],true);
            controller.addSingleRoleToMember(event.getMember(),roleList.get(0)).complete();
        }
    }
}
