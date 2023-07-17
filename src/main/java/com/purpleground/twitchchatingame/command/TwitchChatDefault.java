package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;

public class TwitchChatDefault extends Command {
    public TwitchChatInGame twitchChatInGame;
    public TwitchChatDefault() {
        super("twitchchatdefault", "tcd");
    }

    @Override
    public void handle(String[] args) {
        if(twitchChatInGame.defaultChannel != null){
            String[] message = Arrays.copyOfRange(args,0,args.length);

            twitchChatInGame.client.getChat().sendMessage(twitchChatInGame.defaultChannel, String.join(" ", message));
            String output = String.format("§5[§dTWITCH-CHAT§5] §5%s §6>> §b%s§f: %s", twitchChatInGame.defaultChannel, twitchChatInGame.displayName, String.join(" ", message));
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(output));
        }
        else{
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §4Please set a default channel in settings!"));
        }
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("[%s]", String.join(",", args))));
    }
}
