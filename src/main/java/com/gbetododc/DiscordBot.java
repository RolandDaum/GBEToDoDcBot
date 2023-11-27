package com.gbetododc;

import org.jetbrains.annotations.NotNull;

import com.google.common.util.concurrent.Service.Listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

public class DiscordBot extends ListenerAdapter {


    public static void main() {
        String token = "MTE3MzMwMzAyMzUzNjE3NzI0NA.GRPHm2.b64G6U7kCuZ4LVTEIH4_grWQWdQS6MTzTUiS7U";
        JDA bot = JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new DiscordBot())
            .build();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getMessage().getContentRaw().equals("!ping")) {
            event.getChannel().sendMessage("pong!").queue();
        }

    }

}
