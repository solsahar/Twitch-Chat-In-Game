package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;

public class TwitchRestart extends Command {
    public TwitchChatInGame twitchChatInGame;
    public TwitchRestart() {
        super("twitchrestart");
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
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §6Connecting to twitch..."));
        new Thread(() -> {
            boolean worked = false;
            try {
                worked = twitchChatInGame.getConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(worked){
                twitchChatInGame.connectedToTwitch = twitchChatInGame.connectToTwitch();
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (twitchChatInGame.connectedToTwitch) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §2Connected to twitch..."));
                } else {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§5[§dTWITCH-CHAT§5] §4Failed to connect to twitch..."));
                }
            });
        }).start();
    }
}
