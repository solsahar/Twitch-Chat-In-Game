package com.purpleground.twitchchatingame;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;
import net.weavemc.loader.api.event.*;
import com.purpleground.twitchchatingame.command.*;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.apache.commons.lang.StringUtils;

import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class TwitchChatInGame implements ModInitializer {
    public static class TwitchConfig {
        public String clientId;
        public String clientSecret;
        public String oauth;
        public String[] channelList;
    }

    public ITwitchClient client;
    public String oauthToken = "None";
    public String clientId = "None";
    public String clientSecret = "None";
    public List<String> channels = new ArrayList<>();
    public boolean connectedToTwitch = false;
    public Path getConfigDirPath(){
        return Paths.get(System.getProperty("user.home"), ".weave", "mods", "TwitchChat");
    }
    public Path getConfigPath(){
        return getConfigDirPath().resolve("config.json");
    }
    public void saveConfig() throws IOException {
        fixPaths();

        TwitchConfig config = new TwitchConfig();
        config.clientId = this.clientId;
        config.clientSecret = this.clientSecret;
        config.oauth = this.oauthToken;
        config.channelList = this.channels.toArray(new String[0]);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(getConfigPath().toUri()), config);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void fixPaths() throws IOException {
        var directory = getConfigDirPath();
        var configPath = getConfigPath();
        System.out.println("[CFG-DIR-PATH]: " + directory.toString());
        System.out.println("[CFG-PATH]: " + configPath.toString());
        if(Files.exists(directory) && !Files.isDirectory(directory)){

            Files.delete(directory);
        }
        if(!Files.exists(directory)){
            Files.createDirectory(directory);
        }

        if(Files.exists(configPath) && Files.isDirectory(configPath)){

            Files.delete(configPath);
        }
        if(!Files.exists(configPath)){
            Files.createFile(configPath);
            File cfg = new File(configPath.toUri());
            FileWriter fw = new FileWriter(cfg);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("{}");
            bw.close();
            fw.close();
        }
    }
    public boolean getConfig() throws IOException {
        fixPaths();

        ObjectMapper mapper = new ObjectMapper();
        try {
            TwitchConfig obj = mapper.readValue(new File(getConfigPath().toUri()), TwitchConfig.class);
            this.clientId = obj.clientId;
            this.clientSecret = obj.clientSecret;
            this.oauthToken = obj.oauth;
            this.channels = new ArrayList<>(Arrays.asList(obj.channelList));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void preInit() {
        System.out.println("Initializing StatChecker!");
        System.out.println("Connecting to twitch!");
        new Thread(() -> {
            boolean worked = false;
            try {
                worked = getConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(worked){
                connectedToTwitch = connectToTwitch();
            }
        }).start();

        TwitchConnect twitchConnectCommand = new TwitchConnect();
        twitchConnectCommand.twitchChatInGame = this;
        CommandBus.register(twitchConnectCommand);

        TwitchDisconnect twitchDisconnectCommand = new TwitchDisconnect();
        twitchDisconnectCommand.twitchChatInGame = this;
        CommandBus.register(twitchDisconnectCommand);

        //GetTwitchInfo getTwitchInfoCommand = new GetTwitchInfo();
        //getTwitchInfoCommand.twitchChatInGame = this;
        //CommandBus.register(getTwitchInfoCommand);

        SetTwitchSetting setTwitchSettingCommand = new SetTwitchSetting();
        setTwitchSettingCommand.twitchChatInGame = this;
        CommandBus.register(setTwitchSettingCommand);

        TwitchRestart restartTwitchCommand = new TwitchRestart();
        restartTwitchCommand.twitchChatInGame = this;
        CommandBus.register(restartTwitchCommand);

        TwitchHelp twitchHelpCommand = new TwitchHelp();
        twitchHelpCommand.twitchChatInGame = this;
        CommandBus.register(twitchHelpCommand);

    }
    public static String fixMessageColor(String input) {
        StringBuilder outputBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            outputBuilder.append("§7").append(input.charAt(i));
        }

        return outputBuilder.toString();
    }
    public boolean connectToTwitch(){
        try{
            if(client != null){
                client.close();
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
        try{
            OAuth2Credential credential = StringUtils.isNotBlank(oauthToken) ? new OAuth2Credential("twitch", oauthToken) : null;

            // Build TwitchClient
            client = TwitchClientBuilder.builder()
                    .withClientId(clientId)
                    .withClientSecret(clientSecret)
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    .withEnableHelix(true)
                    .withDefaultAuthToken(credential)
                    .build();
            if (!channels.isEmpty()) {
                channels.forEach(name -> client.getChat().joinChannel(name));
                client.getClientHelper().enableStreamEventListener(channels);
                client.getClientHelper().enableFollowEventListener(channels);
            }

            // Register event listeners
            ITwitchChat chat = client.getChat();
            SimpleEventHandler eventHandler = client.getEventManager().getEventHandler(SimpleEventHandler.class);
            eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }
    private void onChannelMessage(ChannelMessageEvent event){
        String output = String.format("§5[§dTWITCH-CHAT§5] §5%s §6>> §b%s§f: %s", event.getChannel().getName(), event.getUser().getName(), event.getMessage());
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(output));
    }
}