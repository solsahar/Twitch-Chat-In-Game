package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;

public class TwitchChat extends Command {
    public TwitchChatInGame twitchChatInGame;
    public TwitchChat() {
        super("twitchchat", "tc");
    }

    @Override
    public void handle(String[] args) {
        if(args.length >= 2){
            String[] message = Arrays.copyOfRange(args,1,args.length);

            twitchChatInGame.client.getChat().sendMessage(args[0], String.join(" ", message));
            String output = String.format("§5[§dTWITCH-CHAT§5] §5%s §6>> §b%s§f: %s", args[0].toLowerCase(), twitchChatInGame.displayName, String.join(" ", message));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(output));
        }
        else{
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6-------§5Twitch Help: §dTwitchChat§6-------"));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§6/twitchchat§f <channel> <message>: Send a message in a specific twitch channel."));
        }
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("[%s]", String.join(",", args))));
    }
}
