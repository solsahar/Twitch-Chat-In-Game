package com.purpleground.twitchchatingame.command;

import net.weavemc.loader.api.command.Command;
import com.purpleground.twitchchatingame.TwitchChatInGame;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class GetTwitchInfo extends Command {
    public TwitchChatInGame twitchChatInGame;
    public GetTwitchInfo() {
        super("twitchinfo");
    }
    @Override
    public void handle(String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(String.format("ClientID: %s | ClientSecret: %s | oauth: %s", twitchChatInGame.clientId, twitchChatInGame.clientSecret, twitchChatInGame.oauthToken)));
    }
}
