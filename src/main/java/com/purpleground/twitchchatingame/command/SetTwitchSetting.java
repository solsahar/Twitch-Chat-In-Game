package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.Arrays;

public class SetTwitchSetting extends Command {
    public TwitchChatInGame twitchChatInGame;
    public SetTwitchSetting() {
        super("twitchsetting");
    }
    @Override
    public void handle(String[] args) {
        if(args.length == 0){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchSetting§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientid <client id>§f: Set the ClientID."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientsecret <client secret>§f: Set the ClientSecret."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting oauth <oauth token>§f: Set the Oauth token."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels add <channel>§f: Add a channel that will be listened to."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels remove <channel>§f: Remove a channel from the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels clear§f: Clear the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels list§f: List the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Note§f: you must restart the connection from twitch for settings to apply."));
        }
        else{
            if(args[0].equalsIgnoreCase("clientid") && args.length >= 2){
                twitchChatInGame.clientId = args[1];
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Set ClientID!"));
            }
            else if(args[0].equalsIgnoreCase("clientsecret") && args.length >= 2){
                twitchChatInGame.clientSecret = args[1];
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Set ClientSecret!"));
            }
            else if(args[0].equalsIgnoreCase("oauth") && args.length >= 2){
                twitchChatInGame.oauthToken = args[1];
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Set Oauth!"));
            }
            else if(args[0].equalsIgnoreCase("channels") && args.length >= 2){
                if(args[1].equalsIgnoreCase("add")) {
                    if(args.length >= 3){
                        twitchChatInGame.channels.add(args[2].toLowerCase());
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("§5[§dTWITCH-CHAT§5] §2Added %s to channel list!", args[2])));
                    }
                    else{
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §4Missing Channel!"));
                    }
                }else if(args[1].equalsIgnoreCase("remove")){
                    if(args.length >= 3) {
                        boolean worked = twitchChatInGame.channels.remove(args[2].toLowerCase());
                        if (worked){
                            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("§5[§dTWITCH-CHAT§5] §2Removed %s from channel list!", args[2])));
                        }
                        else{
                            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("§5[§dTWITCH-CHAT§5] §4%s did not exist in the channel list!", args[2])));
                        }
                    }
                    else{
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §4Missing Channel!"));
                    }
                }else if(args[1].equalsIgnoreCase("clear")){
                    twitchChatInGame.channels.clear();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Cleared channel list!"));
                }else if(args[1].equalsIgnoreCase("list")){
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Channels: " + twitchChatInGame.channels.toString()));
                }
                else{
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchSetting§6-------"));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientid <client id>§f: Set the ClientID."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientsecret <client secret>§f: Set the ClientSecret."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting oauth <oauth token>§f: Set the Oauth token."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels add <channel>§f: Add a channel that will be listened to."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels remove <channel>§f: Remove a channel from the channel list."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels clear§f: Clear the channel list."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels list§f: List the channel list."));
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Note§f: you must restart the connection from twitch for settings to apply."));
                }
            }
            else{
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchSetting§6-------"));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientid <client id>§f: Set the ClientID."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientsecret <client secret>§f: Set the ClientSecret."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting oauth <oauth token>§f: Set the Oauth token."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels add <channel>§f: Add a channel that will be listened to."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels remove <channel>§f: Remove a channel from the channel list."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels clear§f: Clear the channel list."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels list§f: List the channel list."));
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Note§f: you must restart the connection from twitch for settings to apply."));
            }
            try {
                twitchChatInGame.saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
