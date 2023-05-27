package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;

public class TwitchHelp extends Command {
    public TwitchChatInGame twitchChatInGame;
    public TwitchHelp() {
        super("twitchhelp");
    }
    @Override
    public void handle(String[] args) {
        if(args.length == 0) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dIndex§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchhelp <command>§f: Get help on a specific command."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting§f: Configure the mod to work."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchconnect§f: Connect to twitch."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchdisconnect§f: Disconnect from twitch."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchrestart§f: Restart the connection to twitch."));
        } else if(args[1].equalsIgnoreCase("twitchsetting")){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchSetting§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientid <client id>§f: Set the ClientID."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting clientsecret <client secret>§f: Set the ClientSecret."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting oauth <oauth token>§f: Set the Oauth token."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels add <channel>§f: Add a channel that will be listened to."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels remove <channel>§f: Remove a channel from the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels clear§f: Clear the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchsetting channels list§f: List the channel list."));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6Note§f: you must restart the connection from twitch for settings to apply."));
        } else if (args[1].equalsIgnoreCase("twitchconnect")){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchConnect§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchconnect§f: Connect to twitch and start reading chat."));
        } else if (args[1].equalsIgnoreCase("twitchdisconnect")){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchDisconnect§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchconnect§f: Disconnect from twitch and stop reading chat."));
        } else if (args[1].equalsIgnoreCase("twitchrestart")){
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchRestart§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchrestart§f: Restart the connection to twitch."));
        }
    }
}
