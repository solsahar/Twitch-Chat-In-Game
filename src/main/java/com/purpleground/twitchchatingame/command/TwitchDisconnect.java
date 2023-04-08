package com.purpleground.twitchchatingame.command;

import club.maxstats.weave.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class TwitchDisconnect extends Command {
    public TwitchChatInGame twitchChatInGame;
    public TwitchDisconnect() {
        super("twitchdisconnect");
    }

    @Override
    public void handle(String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §4Disconnected from twitch..."));
        try{
            if(twitchChatInGame.client != null){
                twitchChatInGame.client.close();
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
        twitchChatInGame.connectedToTwitch = false;
    }
}
