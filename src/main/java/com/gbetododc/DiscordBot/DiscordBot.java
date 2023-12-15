package com.gbetododc.DiscordBot;

import com.gbetododc.DiscordBot.Commands.BotCommand;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot extends ListenerAdapter {

    static Dotenv dotenv = Dotenv.configure().load();

    public static void main(String[] args) throws InterruptedException {
        Logger.log("Starting Bot ...", "", LogLvl.Title);

        String TOKEN = dotenv.get("DISCORDBOTTOKEN");

        JDA jda = JDABuilder.createDefault(TOKEN)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(new BotCommand())                            // What to do if a Command has been called
            .build()
            .awaitReady();
        Logger.log("Connected Bot - build", "Connected the bot successfully", LogLvl.Title);

        // Setup the Command Layout
        BotCommand.commandSetup(jda);

    }
}