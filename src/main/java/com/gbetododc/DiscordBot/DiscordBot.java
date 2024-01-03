package com.gbetododc.DiscordBot;

import com.gbetododc.DiscordBot.Commands.BotCommand;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class DiscordBot extends ListenerAdapter {

    static Dotenv dotenv = Dotenv.configure().load();
    public static Guild MAIINSERVERGUILD;
    public static JDA JDA;
    public static String PROJPATH = System.getProperty("user.dir");

    public static void main(String[] args) throws InterruptedException {
        Logger.log("DiscordBot - starting", "Starting the bot", LogLvl.Title);

        String TOKEN = dotenv.get("DISCORDBOTTOKEN");

        JDA jda = JDABuilder.createDefault(TOKEN)
            .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
            .setMemberCachePolicy(MemberCachePolicy.ALL)            
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
            .addEventListeners(new BotCommand())
            .build()
            .awaitReady();
        DiscordBot.JDA = jda;
        DiscordBot.MAIINSERVERGUILD = jda.getGuildById(dotenv.get("MAINSERVERGUILDID"));
        Logger.log("DiscordBot - build", "Connected the bot successfully", LogLvl.Title);

        // Setup the Command Layout
        // BotCommand.commandSetup();
    }
}