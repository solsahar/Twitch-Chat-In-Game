package com.purpleground.twitchchatingame.command;

import club.maxstats.weave.loader.api.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class TestCommand extends Command {
    public TestCommand() {
        super("test");
    }

    @Override
    public void handle(String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("This is a command!"));
    }
}
