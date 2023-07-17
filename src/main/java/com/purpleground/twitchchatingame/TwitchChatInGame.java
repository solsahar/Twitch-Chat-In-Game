package com.purpleground.twitchchatingame;

import com.github.philippheuer.credentialmanager.domain.IdentityProvider;
import com.github.twitch4j.auth.TwitchAuth;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.Command;
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
import org.jetbrains.annotations.Debug;
import org.lwjgl.Sys;


public class TwitchChatInGame implements ModInitializer {

    public static class TwitchConfig {
        public String clientId;
        public String clientSecret;
        public String oauth;
        public String[] channelList;
        public String defaultChannel;
    }
    public String defaultChannel;

    public ITwitchClient client;
    public OAuth2Credential credentials;
    public String displayName;
    public String oauthToken = "None";
    public String clientId = "None";
    public String clientSecret = "None";
    public List<String> channels = new ArrayList<>();

    public Map<String, String> connectedChannels;
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
        config.defaultChannel = this.defaultChannel;
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
            this.defaultChannel = obj.defaultChannel;
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

        GetTwitchInfo getTwitchInfoCommand = new GetTwitchInfo();
        getTwitchInfoCommand.twitchChatInGame = this;
        CommandBus.register(getTwitchInfoCommand);

        SetTwitchSetting setTwitchSettingCommand = new SetTwitchSetting();
        setTwitchSettingCommand.twitchChatInGame = this;
        CommandBus.register(setTwitchSettingCommand);

        TwitchRestart restartTwitchCommand = new TwitchRestart();
        restartTwitchCommand.twitchChatInGame = this;
        CommandBus.register(restartTwitchCommand);

        TwitchHelp twitchHelpCommand = new TwitchHelp();
        twitchHelpCommand.twitchChatInGame = this;
        CommandBus.register(twitchHelpCommand);

        TwitchChat twitchChatCommand = new TwitchChat();
        twitchChatCommand.twitchChatInGame = this;
        CommandBus.register(twitchChatCommand);

        TwitchChatDefault twitchChatDefaultCommand = new TwitchChatDefault();
        twitchChatDefaultCommand.twitchChatInGame = this;
        CommandBus.register(twitchChatDefaultCommand);

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
            connectedChannels = new HashMap<>();
            credentials = StringUtils.isNotBlank(oauthToken) ? new OAuth2Credential("twitch", oauthToken) : null;
            System.out.println(credentials.toString());
            displayName = new TwitchIdentityProvider(null, null, null).getAdditionalCredentialInformation(credentials).map(OAuth2Credential::getUserName).orElse(null);
            // Build TwitchClient
            client = TwitchClientBuilder.builder()
                    .withClientId(clientId)
                    .withClientSecret(clientSecret)
                    .withEnableChat(true)
                    .withChatAccount(credentials)
                    .withEnableHelix(true)
                    .withDefaultAuthToken(credentials)
                    .withEnablePubSub(true)
                    .build();
            System.out.println("Channels in list: " + channels.size());
            if (!channels.isEmpty()) {
                channels.forEach(name -> {
                    client.getChat().joinChannel(name);
                    // Get the channel ID
                    String channelId = client.getHelix()
                            .getUsers(null, null, Collections.singletonList(name))
                            .execute()
                            .getUsers()
                            .get(0)
                            .getId();
                    connectedChannels.put(name, channelId);
                    client.getPubSub().listenForChannelPointsRedemptionEvents(credentials, channelId);
                });

                client.getClientHelper().enableStreamEventListener(channels);
                client.getClientHelper().enableFollowEventListener(channels);
            }
            if (displayName != null){
                String channelId = client.getHelix()
                        .getUsers(null, null, Collections.singletonList(displayName))
                        .execute()
                        .getUsers()
                        .get(0)
                        .getId();
                connectedChannels.put(displayName, channelId);
                client.getPubSub().listenForChannelPointsRedemptionEvents(credentials, channelId);
            }

            // Register event listeners
            ITwitchChat chat = client.getChat();
            SimpleEventHandler eventHandler = client.getEventManager().getEventHandler(SimpleEventHandler.class);
            eventHandler.onEvent(ChannelMessageEvent.class, this::onChannelMessage);
            SimpleEventHandler pubsubEventHandler = client.getPubSub().getEventManager().getEventHandler(SimpleEventHandler.class);
            pubsubEventHandler.onEvent(RewardRedeemedEvent.class, this::onChannelPointRedemption);

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static String findUsernameById(Map<String, String> usernameToID, String targetId) {
        for (Map.Entry<String, String> entry : usernameToID.entrySet()) {
            if (entry.getValue().equals(targetId)) {
                return entry.getKey();
            }
        }
        return null; // Return null if the ID is not found
    }
    private void onChannelPointRedemption(RewardRedeemedEvent event) {
        String output = String.format("§5[§dTWITCH-REDEMPTION§5] §5%s §6>> §b%s§f redeemed %s", findUsernameById(connectedChannels, event.getRedemption().getChannelId()), event.getRedemption().getUser().getDisplayName(), event.getRedemption().getReward().getTitle());
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(output));
    }

    private void onChannelMessage(ChannelMessageEvent event){
        String output = String.format("§5[§dTWITCH-CHAT§5] §5%s §6>> §b%s§f: %s", event.getChannel().getName(), event.getUser().getName(), event.getMessage());
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(output));
    }
}